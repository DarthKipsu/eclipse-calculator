(defproject eclipse "0.1.0-SNAPSHOT"
  :description "Eclipse Calculator service"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/math.numeric-tower "0.0.4"]
                 [compojure "1.3.1"]
                 [ring/ring-defaults "0.1.3"]]
  :plugins [[lein-ring "0.9.1"]
            [lein-kibit "0.0.8"]
            [jonase/eastwood "0.2.1"]
            [lein-bikeshed "0.2.0"]
            [lein-cloverage "1.0.2"]]
  :ring {:handler eclipse.core.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [midje "1.6.3"]
                        [ring-mock "0.1.5"]]
         :plugins [[lein-midje "3.1.3"]]}})
