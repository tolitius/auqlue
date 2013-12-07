(ns auqlue.qa
  (:require [auqlue.thingies :refer [fade-out-in info info->js local-storage? ls-put ls-get]]
            [clojure.string :refer [split]]
            [jayq.core :as jq]
            [cljs.reader :as reader]
            [crate.core :as crate])
  (:use [jayq.core :only [$]])
  (:use-macros [crate.def-macros :only [defpartial]]))


(def $q-input ($ :#q-input))
(def $prezi-id ($ :#prezi-id))
(def $qa-footer ($ :.qa-footer))
(def $vote ($ ".qa .vup"))

(def voted
  (if local-storage?
    (if-let [v (ls-get ":voted")]
      (atom (reader/read-string v))
      (atom {}))))

;; questions

(defpartial questions [qs]
  [:div.container.qa
    (for [{:keys [qid question votes]} qs]
      [:div {"class" (str "row center q-hidden b-" qid)}
        [:div.col-md-8.col-md-offset-2
         [:div.panel.panel-warning 
          [:div.panel-body.panel-heading.col-md-12.q-panel
            [:div.vote-up.col-md-1 
             [:div {"class" (str "vup q-" qid)} [:i.fa.fa-chevron-up]]
             [:div.v-rank votes]]
            [:div.col-md-10.q-text [:span question]]]]]])])

(defn ladder-viz [qs]
  (let [q-blocks (map #($ (str ".b-" (:qid %))) qs)
        h-blocks (map #(-> % (jq/hide)                                 ;; add style display:none
                             (jq/remove-class "q-hidden")) q-blocks)]  ;; remove hidden css class that would override style othewise
    (fade-out-in h-blocks :i-tout 150 :show jq/fade-in)))

(defn show-questions [data]
  (if (seq data)
    (let [qs (reader/read-string data)]
      (.val $q-input "")
      (jq/html $qa-footer (questions (reverse (sort-by :votes qs))))
      (ladder-viz qs))
    (.log js/console "found no questions for this prezi")))

(defn add-question []                                         ;; TODO: mark it voted by the author
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

(defn new-vote? [id qid]
  (not (some #{qid} (@voted id))))

(defn record-vote [id qid]
  (swap! voted assoc id 
         (conj (@voted id) qid))
  (ls-put ":voted" (str @voted)))

(defn update-ranking [data]
  (let [{:keys [id qid votes]} (reader/read-string data)
        prezi-id (.val $prezi-id)]
    (if (= prezi-id (str id)) (do
      (let [$v (jq/next ($ (str ".q-" qid)))]
        (jq/hide $v)
        (jq/html $v votes)
        (jq/fade-in $v))))))

(defn add-vote [q]
  (jq/ajax {:url "/add-vote"
            :data q
            :type "POST"
            :success update-ranking}))

(defn vote-up [e]
  (this-as q
    (let [qid (-> (.-className q) 
                  (split #" ")
                  last
                  (split #"-")
                  last)
          id (.val $prezi-id)]
      (when (new-vote? id qid)
        (record-vote id qid)
        (add-vote {:id id :qid qid})))))

(jq/on $qa-footer :click ".vup" vote-up)

