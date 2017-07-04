(ns process-wrapper.core
  (:require
    [puget.printer :refer [cprint]]
    [clojure.inspector :as inspect :refer [inspect-tree]]
    [process-wrapper.util :refer :all]
    [me.raynes.conch.low-level :as sh]))

(defn get-prediction [process input]

  "pushes input text on stdin, gets the classification for it on stdout"

  ; push the input (should end with a newline)
  (sh/feed-from-string process input)

  ; get the output prediction
  (loop [raw-response (str)]
    (let
      [byte-read (.read (:out process))]
        (assert (not= byte-read -1))
        (let [as-char (char byte-read)]
          (if (= byte-read 10) ; line-feed
            raw-response
            (recur (str raw-response as-char)))))))

(defn attach [command-and-args]
  " starts and returns a process ready to work with "
  (apply sh/proc command-and-args))




