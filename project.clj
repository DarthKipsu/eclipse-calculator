(defproject eclipse "0.1.0-SNAPSHOT"
  :description "Eclipse Calculator service"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/math.numeric-tower "0.0.4"]
                 [compojure "1.3.1"]
                 [ring/ring-defaults "0.1.2"]]
  :plugins [[lein-ring "0.8.13"]]
  :ring {:handler eclipse.core.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [midje "1.6.3"]
                        [ring-mock "0.1.5"]]
         :plugins [[lein-midje "3.1.3"]]}})
