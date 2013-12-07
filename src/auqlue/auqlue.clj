(ns auqlue
  (:require [auqlue.db :refer [existing-title? add-auqlue find-by-title add-q vote-up]]
            [clojure.string :refer [split capitalize lower-case] :as cstr]
            [clojure.tools.logging :refer [info]]
            [clojure.edn :as edn]
            [clj-time.core :refer [now]]
            [clj-time.format :refer :all]))

(defn dehyph [s]
  (apply str (interpose " " 
    (map capitalize (split s #"-")))))

(defn human-date [d]
  (let [dt (parse (formatters :year-month-day) d)]
    (unparse (formatter "dd MMMM yyyy") dt)))

(defn qa-link [date title]
  (str "/qa/" (unparse (formatter "yyyy-MM-dd") date) "/" (cstr/replace title #" " "-")))

(defn create-new-auqlue [{:keys [prezi] :as data}]
  (let [title (lower-case prezi)]
    (if-not (existing-title? title)        ;; TODO: obviously look for an existing auqlue by more than just a "name" in a real DB
      (let [date (now)]                    ;; TODO: date will later be passed in from the calendar widget
        (add-auqlue 
          (assoc (into {} (filter second (dissoc data :prezi)))  ;; removing nil value elements before assoc
                 :date date
                 :title title))
        (qa-link date title))
      {:exists true})))

(defn auqlue-data [d t]                    ;; TODO: lookup will be doneby an event id instead (of its title)
  (let [title (lower-case (dehyph t))]
    (info (str "existing title?: \"" title "\" => " (existing-title? title)))
    (if (existing-title? title)
      (let [{:keys [id presenter event date qs] :as xyz} (find-by-title title)]
        (info "find title: \"" title "\" => " xyz)
        {:sname presenter
         :id id
         :cname event
         :date (human-date d) 
         :title (dehyph t)
         :qs (reverse (sort-by :votes qs))})
      {:not-found true})))

(defn add-question [id q]
  (add-q (edn/read-string id) q))

(defn add-vote [id qid]
  (let [id (edn/read-string id)
        qid (edn/read-string qid)]
    (if (and id qid)
      (let [votes (vote-up id qid)]
        {:id id :qid qid :votes votes}))))
