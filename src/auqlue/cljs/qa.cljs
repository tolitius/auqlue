(ns auqlue.qa
  (:require [jayq.core :as jq]
            [cljs.reader :as reader]
            [crate.core :as crate])
  (:use [jayq.core :only [$]])
  (:use-macros [crate.def-macros :only [defpartial]]))


(def $q-input ($ :#q-input))
(def $prezi-id ($ :#prezi-id))

;; questions

(defpartial questions [qs]
  [:div.container.qa
    (for [{:keys [qid question votes]} qs]
      [:div.row.center
        [:div.col-md-8.col-md-offset-2
         [:div.panel.panel-warning 
          [:div.panel-body.panel-heading.col-md-12.q-panel
            [:div.vote-up.col-md-1 
             [:div {"class" (str qid)} [:i.fa.fa-chevron-up]]
             [:div.v-rank votes]]
            [:div.col-md-10.q-text [:span question]]]]]])])

(defn show-questions [data]
  (if (seq data)
    (let [qs (reader/read-string data)]
      (.log js/console "questions: " (clj->js qs))
      (.log js/console "q-partial: " (questions qs))
      (jq/html ($ :.qa-footer) (questions qs)))
    (.log js/console "found no questions for this prezi")))

(defn add-question []
  (let [q (.val $q-input)
        id (.val $prezi-id)]
    (.log js/console "prezi id: " id ", question: " q)
    (jq/ajax {:url "/add-question"
              :data {:q q :id id}
              :type "POST"
              :success show-questions})))

(jq/on $q-input :keydown
  (fn [e]
    (if (= (.-keyCode e) 13)
      (do
        (.preventDefault e)
        (add-question)))))

;; voting

(defn update-ranking [data]
  (let [votes (reader/read-string data)]
    (.log js/console "votes: " votes)))

(defn add-vote [q]
  (jq/ajax {:url "/add-vote"
            :data q
            :type "POST"
            :success update-ranking}))

(defn vote-up [e]
  (this-as q
    (.log js/console "voting up: " q)))

(jq/on ($ :.q42) :click vote-up)

