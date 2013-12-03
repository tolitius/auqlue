(ns auqlue
  (:require [clojure.string :refer [split capitalize] :as cstr]
            [clj-time.core :refer [now]]
            [clj-time.format :refer :all]))

;; primitive "auqlueDb" (need to also call it NoSQL, and get crazy following of NoSQL hipsters :))

(defonce events (atom {}))   ;; {"Simple Made Easy" {:presenter "Rich Hickey" :event "Chariot Day" :date date}}
(defonce qs (atom {}))       ;; {"Simple Made Easy" [{:votes v1 :question q1} 
                             ;;                      {:votes v2 :question q2} ... ]}

(defn dehyph [s]
  (apply str (interpose " " 
    (map capitalize (split s #"-")))))

(defn human-date [d]
  (let [dt (parse (formatters :year-month-day) d)]
    (unparse (formatter "dd MMMM yyyy") dt)))

(defn qa-link [date title]
  (str "/qa/" (unparse (formatter "yyyy-MM-dd") date) "/" (cstr/replace title #" " "-")))

(defn create-new-auqlue [{:keys [prezi] :as data}]
  (if-not (@events prezi)                ;; TODO: obviously look for an existing auqlue by more than just a "name" in a real DB
    (let [date (now)]                    ;; TODO: date will later be passed in from the calendar widget
      (swap! events assoc prezi          ;; TODO: and of course it is case sensitive (it's pre alpha, ok!?)
             (assoc 
               (into {} (filter second (dissoc data :prezi)))  ;; removing nil value elements before assoc
               :date date))
      (println "events: " @events)
      (qa-link date prezi))
    {:exists true}))

(defn auqlue-data [date title]
  {:sname "Rich Hickey" 
   :cname "Chariot Day" 
   :date (human-date date) 
   :title (dehyph title)})
