(ns process-wrapper.core-test
  (:require [clojure.test :refer :all]
            [process-wrapper.core :refer :all]))

(deftest a-test
   (attach ["executable/fastText/fasttext"
            "predict-prob"
            "executable/classifier.bin"
            "-"
            "5"]))
