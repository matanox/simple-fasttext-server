(ns process-wrapper.core
  (:require
    [clojure.test :refer :all]
    [puget.printer :refer [cprint]]
    [clojure.inspector :as inspect :refer [inspect-tree]]
    [process-wrapper.util :refer :all]
    [me.raynes.conch.low-level :as sh]
    [instaparse.core :as insta]))


(def ^:private fasttext-output-parser
  (insta/parser
    "Labels = Label-Prediction (Space Label-Prediction)*

     Label-Prediction = Label Space Probability

     <Label> = <Label-Prefix> Label-Name
     <Label-Prefix> = '__label__'
     Label-Name = #'^\\S*'

     <Space> = <' '>

     <Probability> = Probability-Fraction | Probability-Fraction-Scientific-Notation | '0' | '1'

     Probability-Fraction = #'0\\.[0-9]+'
     Probability-Fraction-Scientific-Notation = #'[0-9]+\\.[0-9]+e-[0-9]+'"))


(with-test
  (defn ^:private fasttext-output-parse [text]
    " parses fasttext output returned for a single text object "
    (let
      [parse-or-error (fasttext-output-parser text)]
      (if (insta/failure? parse-or-error)
        (do
          (println parse-or-error)
          (throw (Exception. (str "failed parsing what has been assumed to be fasttext prediction output: " text))))
        parse-or-error)))
  ;; test code
  (is (= 1 1)))


(defn get-prediction [process input]

  "pushes input text on stdin, gets the classification for it on stdout"

  ; push the input (should end with a newline)
  (sh/feed-from-string process input)

  ; get the output prediction
  (let
    [rawparse-or-error
      (loop [raw-response (str)]
        (let
          [byte-read (.read (:out process))]
            (assert (not= byte-read -1))
            (let [as-char (char byte-read)]
              (if (= byte-read 10) ; line-feed
                raw-response
                (recur (str raw-response as-char))))))]

    (fasttext-output-parse rawparse-or-error)))


(defn attach [command-and-args]

  " starts and returns a process ready to work with "

  (apply sh/proc command-and-args))




