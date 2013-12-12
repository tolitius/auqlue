(defproject auqlue "0.1.0-SNAPSHOT"
  :description "42 for all your presentations"
  :url "https://github.com/tolitius/auqlue"

  :source-paths ["src" "src/auqlue"]

  :dependencies [[clj-time "0.6.0"]
                 [compojure "1.1.6"]
                 [hiccup "1.0.4"]
                 [crate "0.2.4"]
                 [jayq "2.5.0"]
                 ;; [com.datomic/datomic-free "0.9.4331" :exclusions [org.slf4j/slf4j-nop
                 ;;                                                   org.slf4j/slf4j-log4j12]]
                 [com.datomic/datomic-free "0.9.4331"]
                 [org.clojure/tools.logging "0.2.6"]
                 [org.clojure/clojurescript "0.0-2030"]
                 [org.clojure/clojure "1.5.1"]]

  :plugins [[lein-ring "0.8.8"]
            [lein-cljsbuild "1.0.0-alpha2"]]

  :hooks [leiningen.cljsbuild]

  :cljsbuild {
    :builds [{:source-paths ["src/auqlue/cljs"]
              :compiler {:output-to "resources/public/js/auqlue.js"
                         :optimizations :whitespace
                         :pretty-print true
                         :source-map "resources/public/js/auqlue.js.map"
                         }}]}

  :ring {:handler auqlue.handler/app}

  :profiles
    {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                          [ring-mock "0.1.5"]]}})
