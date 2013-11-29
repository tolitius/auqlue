(ns auqlue.layout.qa
  (:require [auqlue.layout.base :refer :all]
            [clojure.string :as cstr]))

(defn header [{:keys [title date sname cname]}]
  (in-container {:container-class "qa" :jumbo-class "qa-header"}
    [:div.row.center
     (if cname [:span.qa-conf-name cname])
     [:span.qa-title-header title]
     [:span.qa-date date]]))

(defn address-speaker [sname]
  (if sname
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

(defn questions [{:keys [pid]}]
  (in-container {:container-class "qa" :jumbo-class "qa-footer"}
    [:div.row.center
      [:div.col-md-8.col-md-offset-2
       [:div.panel.panel-warning 
        [:div.panel-body.panel-heading.col-md-12.q-panel
          [:div.vote-up.col-md-1 
           [:div [:i.fa.fa-chevron-up]]
           [:div.v-rank "42"]]
          [:div.col-md-10.q-text [:span "WTF is Monad?"]]]]]]
    [:div.row.center
      [:div.col-md-8.col-md-offset-2
       [:div.panel.panel-warning 
        [:div.panel-body.panel-heading.col-md-12.q-panel
          [:div.vote-up.col-md-1 
           [:div [:i.fa.fa-chevron-up]]
           [:div.v-rank "23"]]
          [:div.col-md-10.q-text [:span "This would be a long question, so let me start by saying.."]]]]]]))

(defn qa-for [{:keys [date title sname cname] :as about}]
  (with-bootstrap (str "auqlue: " title)
    (header about)
    (ask about)
    (questions about)))
