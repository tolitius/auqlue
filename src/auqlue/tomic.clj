(ns auqlue.tomic
  (:use [datomic.api :only (db) :as d])
  (:require [auqlue.dbnator :refer :all]
            [clojure.set :refer [rename-keys]]
            [clojure.tools.logging :refer [info]]))

;; simple in-memory "auqlueDb"

(defonce events (atom {}))      ;; {34 {:title "Simple Made Easy" :presenter "Rich Hickey" :event "Chariot Day" :date date}
                                ;;  42 {:title "Functional for Orinary Smart People" :presenter "Anatoly and Dan" :event "Chariot Day" :date date}}

(defonce qs (atom {}))          ;; {34 [{:qid 1 :votes v1 :question q1} 
                                ;;      {:qid 2 :votes v2 :question q2} ... ]
                                ;;  42 [{:qid 1 :votes v3 :question q3}
                                ;;      {:qid 2 :votes v3 :question q4}... ] ...}

(defn existing-title? [t]
  (-> (q '[:find ?e :in $ ?t :where [?e :prezi/title ?t]] t)
      ffirst))

;; TODO: in reality this will return multiple talks (need to change UI to handle it first)
;; TODO: externalize IDs (e.g. instead of exposing entity IDs, have some two way transaltion logic)
(defn find-by-title [t] 
  (if-let [talk (-> (q '[:find ?e :in $ ?t :where [?e :prezi/title ?t]] t) touch first)]
    (let [questions (for [q (:prezi/questions talk)] 
                      (assoc (de-namespace q) :qid (:db/id q)))]
      (-> talk de-namespace 
          (dissoc :questions) 
          (assoc :id (:db/id talk) :qs questions)))))

;; (defn add-auqlue [auqlue]    ;; trusts a duplicate check that was done before
;;   (swap! events assoc (next-event-pk) auqlue)
;;   (info "added an auqlue to events:" @events))
;; 
;; (defn add-q [id q]           ;; trusts a duplicate check that was done before
;;   (if-let [qcoll (@qs id)]
;;     (swap! qs assoc id 
;;            (conj (@qs id) 
;;                  {:qid (next-q-pk id) :question q :votes 1}))
;;     (swap! qs assoc id [{:qid 0 :question q :votes 1}]))      ;; if this is the first question, create the vec
;;   (let [questions (@qs id)
;;         added (dec (next-q-pk id))]
;;     (info (str "questions for [" id "]:" questions "\n [" added "] was just added"))
;;     {:added added :qs questions}))
;; 
;; (defn vote-up [id qid]
;;   (let [prezi-qs (@qs id)
;;         current-votes (get-in prezi-qs [qid :votes])]
;;     (swap! qs assoc id 
;;            (assoc-in prezi-qs [qid :votes] (inc current-votes)))
;;     (inc current-votes)))
