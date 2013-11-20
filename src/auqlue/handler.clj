(ns auqlue.handler
  (:require [auqlue.layout 
             [create :refer [create-auqlue]]
             [qa :refer [qa-for]]]
            [clojure.string :refer [split capitalize]]
            [clj-time.format :refer :all]
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]))

(defn dehyph [s]
  (apply str (interpose " " 
    (map capitalize (split s #"-")))))

(defn human-date [d]
  (let [dt (parse (formatters :year-month-day) d)]
    (unparse (formatter "dd MMMM yyyy") dt)))

(defroutes app-routes
  (GET "/" [] (create-auqlue))
  (GET "/qa/:date/:title" [date title] 
       (qa-for {:sname "Rich Hickey" 
                :cname "Chariot Day" 
                :date (human-date date) 
                :title (dehyph title)}))
  (route/resources "/")
  (route/not-found "not found"))

(def app
  (handler/site app-routes))
