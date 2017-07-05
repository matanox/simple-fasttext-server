(ns process-wrapper.main
  (:require
     [yada.yada :refer [listener resource as-resource]]
     [process-wrapper.core :refer :all]))


(defn -main []

  (println "server starting")

  (let
    [process (attach ["fasttext/fastText/fasttext" "predict-prob" "fasttext/classifier.bin" "-" "5"])
     classify
       (fn [ctx]
          (let [text (str (get-in ctx [:parameters :query :text]) "\n")]
             (get-prediction process text)))]

    (def yada-server
      (listener
         ["/"
           [["status"
               (resource
                  {:produces "text/plain"
                   :response "Server is up"})]
            ["predict"
               (resource
                 {:methods
                    {:get
                      {:parameters {:query {:text String}}
                       :produces "text/plain"
                       :response classify}}})]
            [true (as-resource nil)]]]

       {:port 3000})))

  @(promise)) ;; block forever to keep the yada server alive
