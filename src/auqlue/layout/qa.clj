(ns auqlue.layout.qa
  (:require [auqlue.layout.base :refer :all]
            [clojure.string :as cstr]
            [hiccup.core]))

(defn header [{:keys [title date sname cname id]}]
  (in-container {:container-class "qa" :jumbo-class "qa-header"}
    [:div.row.center
     (if cname [:span.qa-conf-name cname])
     [:input {"id" "prezi-id" "type" "hidden" "value" id}]
     [:span.qa-title-header title]
     [:span.qa-date date]]))

(defn address-speaker [sname]
  (if (seq sname)
    (-> (cstr/split sname #" ")
        first)))

(defn q-title [sname]
  (if-let [fname (address-speaker sname)]
    (str fname ", can you please tell me")
    "Dear presenter, can you please tell me"))

(defn ask [{:keys[sname]}]
  (in-container {:container-class "qa"}
    [:div.row.center
     [:form {:role "form"}
      [:div.form-group.col-md-8.col-md-offset-2
       [:p.q-title (q-title sname)]
       [:input.form-control {:id "q-input" :autofocus "autofocus" :placeholder "e.g. what is, why, how to, where, when, etc.."}]]]]))

(defn show-qs [qs]
  (for [{:keys [qid question votes]} qs]
    [:div {"class" (str "row center b-" qid)}
      [:div.col-md-8.col-md-offset-2
       [:div.panel.panel-warning 
        [:div.panel-body.panel-heading.col-md-12.q-panel
          [:div.vote-up.col-md-1 
           [:div {"class" (str "vup q-" qid)} [:i.fa.fa-chevron-up]]
           [:div.v-rank votes]]
          [:div.col-md-10.q-text [:span question]]]]]]))

(defn q-blank []
  [:div.row.center
    [:div.col-md-8.col-md-offset-2
     [:div.panel.panel-warning 
      [:div.panel-body.panel-heading.col-md-12.q-panel
        [:div.vote-up.col-md-1 
         [:div [:i.fa.fa-angle-double-right]]
         [:div.v-rank [:i.fa.fa-angle-double-right]]]
        [:div.col-md-10.q-text [:span.center "Be The First One To Ask a Question!"]]]]]])


(defn questions [{:keys [qs]}]
  (in-container {:container-class "qa" :jumbo-class "qa-footer"}
    (if (seq qs)
      (show-qs qs)
      (q-blank))))

(defn qa-for [{:keys [title] :as about}]
  (with-bootstrap (str "auqlue: " title)
    (header about)
    (ask about)
    (questions about)))
