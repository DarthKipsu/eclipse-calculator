(ns eclipse.core.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.adapter.jetty :as ring]
            [eclipse.core.core :refer :all]
            [clojure.data.json :as json])
  (:use [ring.middleware.params :only [wrap-params]]
        [ring.middleware.json :refer :all]
        [ring.middleware.keyword-params :only [wrap-keyword-params]])
  (:gen-class))

(defn- error [code message]
  {:status code
   :headers {"Content-Type" "application/json"
             "Access-Control-Allow-Origin" "*"}
   :body {:error message}})

(defroutes app-routes
  (GET "/" []
       {:status 302
        :headers {"Location" "http://darth.kipsu.fi/EclipseCalculator"}
        :body ""})
  (GET "/odds" {params :params}
       (cond
         (empty? params) (error 400 "request cannot be empty")
         :else
           (try {:status 200
                 :headers {"Content-Type" "application/json"
                           "Access-Control-Allow-Origin" "*"}
                 :body (win-probabilities (reform-ships params))}
             (catch Exception e
               (error 500 (str "caught exception: " (.getMessage e)))))))
  (route/not-found "Not Found"))

(def app
  (routes (-> app-routes
            wrap-json-response
            wrap-json-params
            wrap-keyword-params
            wrap-params)))

(defn start [port]
  (ring/run-jetty app {:port port :join? false}))

(defn -main []
  (let [port (Integer. (or (System/getenv "PORT") "8080"))]
    (start port)))
