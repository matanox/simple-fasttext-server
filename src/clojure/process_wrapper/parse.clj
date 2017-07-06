(ns process-wrapper.parse
  (:require
    [puget.printer :refer [cprint]]
    [clojure.inspector :as inspect :refer [inspect-tree]]
    [process-wrapper.util :refer :all]
    [instaparse.core :as insta]
    [clojure.test :refer :all]))


(def ^:private fasttext-output-parser
  (insta/parser
    "Labels = Scored-Label (Space Scored-Label)*

     Scored-Label = Label Space Score

     <Label> = <Label-Prefix> Label-Name
     <Label-Prefix> = '__label__'
     <Label-Name> = #'^\\S*'

     <Space> = <' '>

     <Score> = Score-Fraction | Score-Fraction-Scientific-Notation | '0' | '1'

     <Score-Fraction> = #'0\\.[0-9]+'
     <Score-Fraction-Scientific-Notation> = #'[0-9]+\\.[0-9]+e-[0-9]+'"))


(with-test
  (defn fasttext-output-parse [text]
    " parses fasttext output returned for a single text object "
    (let
      [parse-or-error (fasttext-output-parser text)]
      (if (insta/failure? parse-or-error)
        (do
          (println parse-or-error)
          (throw (Exception. (str "failed parsing what has been assumed to be fasttext prediction output: " text))))
        parse-or-error)))

  ;; tests
  (is (=
     (fasttext-output-parse "__label__A 1 __label__B 1.95313e-08 __label__C 1.95313e-08 __label__D 1.95313e-08 __label__E 1.95313e-08")
     [:Labels
        [:Scored-Label "A" "1"]
        [:Scored-Label "B" "1.95313e-08"]
        [:Scored-Label "C" "1.95313e-08"]
        [:Scored-Label "D" "1.95313e-08"]
        [:Scored-Label "E" "1.95313e-08"]]))

  (is (=
     (fasttext-output-parse "__label__A 0.999998")
     [:Labels
        [:Scored-Label "A" "0.999998"]])))


