(ns process-wrapper.core
  (:require
    [clojure.test :refer :all]
    [puget.printer :refer [cprint]]
    [clojure.inspector :as inspect :refer [inspect-tree]]
    [process-wrapper.util :refer :all]
    [process-wrapper.parse :refer :all]
    [me.raynes.conch.low-level :as sh]
    [instaparse.core :as insta]))


(def worker-status (atom {})) ;; for now, only manages a single worker

(defn attach [command-and-args]

  " starts and returns a process ready to work with "

  (let
     [conch-object (apply sh/proc command-and-args) ; this is the map returned by `conch/sh`, consisting of the keys: :err :in :out :process
      process-object (:process conch-object)        ; the java process object
      new-worker
        {process-object
          {:shell-object conch-object
           :status :idle}}]

     (swap! worker-status conj new-worker)

     new-worker))

(defn ^:private get-worker-state [worker]
  (get-in (val (first worker)) [:status]))

(add-watch worker-status :watcher
  (fn [watch-name watch-object old new]
    (println "watch activated")
    #_(do
       (println "changed from")
       (cprint old-state)
       (println "to")
       (cprint new-state))))

(defn get-prediction [worker input]

  "pushes input text on stdin, gets the classification for it on stdout"

  (swap! worker-status
     (fn swap-fn [worker-status]
        (update-in worker-status [(key (first worker)) :status] (constantly :working))))

  ; push the input (should end with a newline)

  (sh/feed-from-string (get-in (val (first worker)) [:shell-object]) input)

  ; get the output prediction
  (let
    [rawparse-or-error
      (loop [raw-response (str)]
        (let
          [byte-read (.read (get-in (val (first worker)) [:shell-object :out]))]
            (assert (not= byte-read -1))
            (let [as-char (char byte-read)]
              (if (= byte-read 10) ; line-feed
                raw-response
                (recur (str raw-response as-char))))))

    parse (fasttext-output-parse rawparse-or-error)]

    (swap! worker-status
      (fn [process-list]
         (update-in process-list [(key (first worker)) :status] (constantly :idle))))

    parse))



