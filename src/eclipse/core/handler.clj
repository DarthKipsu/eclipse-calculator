(ns eclipse.core.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [eclipse.core.core :refer :all]
            [clojure.data.json :as json])
(:use [ring.middleware.params :only [wrap-params]]
      [ring.middleware.json :refer :all]
      [ring.middleware.keyword-params :only [wrap-keyword-params]]))

(defn- error [message]
  {:status 400
   :headers {"Content-Type" "application/json"}
   :body {:error message}})

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/odds" {params :params}
       (cond
         (empty? params) (error "request cannot be empty")
         :else
           {:status 200
            :headers {"Content-Type" "application/json"}
            :body (win-probabilities (reform-ships params))}))
  (route/not-found "Not Found"))

(def app
  (routes (-> app-routes
            wrap-json-response
            wrap-json-params
            wrap-keyword-params
            wrap-params)))
