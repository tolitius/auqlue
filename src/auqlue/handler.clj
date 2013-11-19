(ns auqlue.handler
  (:require [auqlue.layout.create :refer [create-auqlue]]
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes app-routes
  (GET "/" [] (create-auqlue))
  (route/resources "/")
  (route/not-found "not found"))

(def app
  (handler/site app-routes))
