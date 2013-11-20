(ns auqlue.layout.qa
  (:require [auqlue.layout.base :refer :all]))

(defn header [{:keys [title date sname cname]}]
  (in-container {:container-class "qa" :jumbo-class "qa-header"}
    [:div.row.center
     [:p.qa-title-header title]
     (if cname [:p.qa-conf-name cname])
     [:p.qa-date date]]))

(defn ask [{:keys[sname]}]
  (in-container {:container-class "create-auqlue"}
    [:div.row.center
     [:form {:role "form"}
      [:div.form-group.col-md-6.col-md-offset-3
       [:label.prezi-name {:for "prezi-name"} "Presentation Name"]
       [:div.input-group.margin-bottom-sm
         [:input.form-control {:id "prezi-input" :autofocus "autofocus" :placeholder "name will be used in a link"}]
         [:span.input-group-addon.cal-icon
          [:i.fa.fa-calendar]]]]
      [:div.form-group.col-md-6.col-md-offset-3
       [:button.btn.btn-lg.btn-warning {:type "submit" :id "new-auqlue"} "Create Auqlue"]]]]))

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
