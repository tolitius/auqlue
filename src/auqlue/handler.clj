(ns auqlue.handler
  (:require [auqlue :refer [create-new-auqlue auqlue-data add-question]]
            [auqlue.layout 
             [create :refer [create-auqlue]]
             [qa :refer [qa-for]]]
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes app-routes
  (GET "/" [] (create-auqlue))
  (GET "/qa/:date/:title" [date title]    ;; TODO: will need somm more unique (most likely just a unique link)
       (qa-for (auqlue-data date title)))
  (POST "/new-auqlue" {auqlue :params} (str (create-new-auqlue auqlue)))
  (POST "/add-question" [id q] (str (add-question id q)))
  (route/resources "/")
  (route/not-found "not found"))

(def app
  (handler/site app-routes))
