(defproject process-wrapper "0.0.1"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"

  :source-paths      ["src/clojure"]
  :java-source-paths ["src/java"]
  :javac-options     ["-target" "1.8" "-source" "1.8"]

  :dependencies [[org.clojure/clojure "1.8.0"]

                 [me.raynes/conch "0.8.0"]         ; proper shelling out for clojure

                 ; yada
                 [yada "1.2.6"]
                 [aleph "0.4.1"]
                 [bidi "2.0.12"]

                 [instaparse "1.4.7"]

                 ;[com.taoensso/timbre "4.10.0"]   ; clojure logging

                 [org.slf4j/slf4j-simple "1.7.25"] ; for the draining of sl4fj-reliant libraries used here

                 [clj-time "0.13.0"]               ; https://github.com/clj-time/clj-time

                 [cheshire "5.7.1"]                ; for working with json

                 [org.clojure/data.csv "0.1.3"]    ; for csv

                 [org.clojure/math.combinatorics "0.1.4"]

                 [io.aviso/pretty "0.1.33"] ; pretty exceptions in leinigen (http://ioavisopretty.readthedocs.io/en/latest/lein-plugin.html)
                 [mvxcvi/puget "1.0.1"]]    ; color printing function (https://github.com/greglook/puget#usage), see `with-color` and `cprint`

  :profiles {:dev {:dependencies
                    [[org.clojure/tools.trace "0.7.5"]
                     [criterium "0.4.3"]
                     [rhizome "0.2.5"]
                     ;[org.noisesmith.poirot :as poirot]
                     ]}}

  :plugins [[io.aviso/pretty "0.1.33"]      ; pretty exceptions in leinigen, needed here as well as in :dependencies
            [mvxcvi/whidbey "1.3.1"]        ; more colorful repl (https://github.com/greglook/whidbey)
            [lein-codox "0.10.3"]
            [lein-auto "0.1.3"]]   ; provides the auto lein command for watching source changes

  ; this doesn't work yet ― see https://github.com/weavejester/lein-auto/issues/6
  ; :auto {:default {:paths (:source-paths :java-source-paths :test-paths :java-source-paths "my path")}} ; https://github.com/weavejester/lein-auto#usage

  :codox {:metadata {:doc/format :markdown}} ; treat docstrings as codox extended markdown (https://github.com/weavejester/codox/blob/master/example/src/clojure/codox/markdown.clj)

  :main process-wrapper.main)
