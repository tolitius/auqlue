(ns auqlue.layout.qa
  (:require [auqlue.layout.base :refer :all]
            [clojure.string :as cstr]))

(defn header [{:keys [title date sname cname]}]
  (in-container {:container-class "qa" :jumbo-class "qa-header"}
    [:div.row.center
     [:p.qa-title-header title]
     (if cname [:p.qa-conf-name cname])
     [:p.qa-date date]]))

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
      [:div.form-group.col-md-6.col-md-offset-3
       [:p.q-title (q-title sname)]
       [:input.form-control {:id "q-input" :autofocus "autofocus" :placeholder "e.g. what is, why, how to, where, when, etc.."}]]]]))

(defn questions [{:keys [pid]}]
  (in-container {:container-class "create-auqlue" :jumbo-class "o-footer"}
    [:div.row.center
     [:form {:role "form"}
      ;; [:div.form-group.col-md-6.col-md-offset-3.o-label
      ;;     [:span.label.label-success "optional"]]
      [:div.form-group.col-md-6.col-md-offset-3
       [:div.panel.panel-warning 
        [:div.panel-body.panel-heading
          [:form.form-inline.center {:role "form"}
           [:div.form-group.col-md-6
            [:label {:for "presenter-name"} "Presenter's Name"]
            [:input.form-control {:id "presenter-input" :placeholder "\"address\" the presenter"}]]
           [:div.form-group.col-md-6
            [:label {:for "conference-name"} "Conference Name"]
            [:input.form-control {:id "conference-input" :placeholder "relate it to the conference"}]]]]]]]]))

(defn qa-for [{:keys [date title sname cname] :as about}]
  (with-bootstrap (str "auqlue: " title)
    (header about)
    (ask about)
    (questions about)))
