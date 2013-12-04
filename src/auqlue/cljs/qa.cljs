(ns auqlue.qa
  (:require [auqlue.thingies :refer [fade-out-in info info->js]]
            [jayq.core :as jq]
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
      [:div {"class" (str "row center q-hidden b-" qid)}
        [:div.col-md-8.col-md-offset-2
         [:div.panel.panel-warning 
          [:div.panel-body.panel-heading.col-md-12.q-panel
            [:div.vote-up.col-md-1 
             [:div {"class" (str "q-" qid)} [:i.fa.fa-chevron-up]]
             [:div.v-rank votes]]
            [:div.col-md-10.q-text [:span question]]]]]])])

(defn ladder-viz [qs]
  (let [q-blocks (map #($ (str ".b-" (:qid %))) qs)
        h-blocks (map #(-> % (jq/hide)                                 ;; add style display:none
                             (jq/remove-class "q-hidden")) q-blocks)]  ;; remove hidden css class that would override style othewise
    (fade-out-in h-blocks :i-tout 150 :show jq/fade-in)))

(defn show-questions [data]
  (.log js/console "data: " data)
  (if (seq data)
    (let [qs (reader/read-string data)]
      (.val $q-input "")
      (.log js/console "questions: " (clj->js qs))
      (.log js/console "q-partial: " (questions qs))
      (jq/html ($ :.qa-footer) (questions qs))
      (ladder-viz qs))
    (.log js/console "found no questions for this prezi")))

(defn add-question []
  (let [q (.val $q-input)
        id (.val $prezi-id)]
    (.log js/console "prezi id: " id ", question: " q)
    (if (seq q)
      (jq/ajax {:url "/add-question"
                :data {:q q :id id}
                :type "POST"
                :success show-questions}))))

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
