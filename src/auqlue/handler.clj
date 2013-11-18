(ns auqlue.handler
  (:use compojure.core
        auqlue.views)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes app-routes
  (GET "/" [] (create-auqlue))
  (route/resources "/")
  (route/not-found "not found"))

(def app
  (handler/site app-routes))
