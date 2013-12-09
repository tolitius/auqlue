(ns auqlue.db
  (:require [clojure.tools.logging :refer [info]]))

;; simple in-memory "auqlueDb"

(defonce events (atom {}))      ;; {34 {:title "Simple Made Easy" :presenter "Rich Hickey" :event "Chariot Day" :date date}
                                ;;  42 {:title "Functional for Orinary Smart People" :presenter "Anatoly and Dan" :event "Chariot Day" :date date}}

(defonce qs (atom {}))          ;; {34 [{:qid 1 :votes v1 :question q1} 
                                ;;      {:qid 2 :votes v2 :question q2} ... ]
                                ;;  42 [{:qid 1 :votes v3 :questions q3}
                                ;;      {:qid 2 :votes v3 :questions q4}... ] ...}

(defn next-event-pk []
  (if (seq @events)
    (inc (apply max (keys @events)))
    0))

(defn next-q-pk [eid]
  (if (seq @qs)
    (inc (apply max 
             (map :qid (@qs eid))))
    0))

(defn existing-title? [t]
  (some #{t} 
        (for [[_ {:keys [title]}] @events] title)))

(defn find-by-title [t]                                           ;; here is your slow (no index), but pretty join
  (if-let [id (-> (for [[k v] @events :when (= t (:title v))] k)
                  first)]
    (assoc (@events id) :qs (@qs id) :id id)))

(defn add-auqlue [auqlue]    ;; trusts a duplicate check that was done before
  (swap! events assoc (next-event-pk) auqlue)
  (info "added an auqlue to events:" @events))

(defn add-q [id q]           ;; trusts a duplicate check that was done before
  (if-let [qcoll (@qs id)]
    (swap! qs assoc id 
           (conj (@qs id) 
                 {:qid (next-q-pk id) :question q :votes 1}))
    (swap! qs assoc id [{:qid 0 :question q :votes 1}]))      ;; if this is the first question, create the vec
  (let [questions (@qs id)
        added (dec (next-q-pk id))]
    (info (str "questions for [" id "]:" questions "\n [" added "] was just added"))
    {:added added :qs questions}))

(defn vote-up [id qid]
  (let [prezi-qs (@qs id)
        current-votes (get-in prezi-qs [qid :votes])]
    (swap! qs assoc id 
           (assoc-in prezi-qs [qid :votes] (inc current-votes)))
    (inc current-votes)))
