(ns auqlue.views  
  (:require
    [hiccup
      [page :refer [html5 include-js include-css]]]))

(defn with-css []
  (list
   (include-css "/bootstrap/css/bootstrap.css")
   (include-css "/bootstrap/css/bootstrap-theme.css")
   (include-css "/css/font-awesome.min.css")
   (include-css "/css/auqlue.css")))

(defn with-js []
  (list
   (include-js "/js/jquery-1.10.2.min.js")
   (include-js "/bootstrap/js/bootstrap.js")
   (include-js "/js/auqlue.js")))

(defn in-container [{:keys [container-class jumbo-class]} & content]
  (let [c [:div {"class" (str "container " container-class)} content]]
    (if jumbo-class
      [:div {:class (str "jumbotron " jumbo-class)} c]
      c)))

(defn with-bootstrap [title & content]
  (html5
    [:head
      [:title title]
      [:meta {"name" "viewport" "content" "width=device-width, initial-scale=1.0"}]
      (with-css)]
    [:body
      content
      (with-js)]))



(defn create-auqlue []
  (with-bootstrap "Answer to the Ultimate Question of Life, the Universe, and Everything"

    (in-container {:container-class "create-auqlue" :jumbo-class "c-header"}
      [:div.row.center
       [:p.a-header.auqlue-color "auqlue"]
       [:h5.create-auqlue 
        [:span.an.auqlue-color "A"] "nswer to the "
        [:span.ul.auqlue-color "U"] "ltimate "
        [:span.qu.auqlue-color "Q"] "uestion of "
        [:span.li.auqlue-color "L"] "ife, the "
        [:span.un.auqlue-color "U"] "niverse, and "
        [:span.ev.auqlue-color "E"] "verything"]])

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
         [:button.btn.btn-lg.btn-warning {:type "submit" :id "new-auqlue"} "Create Auqlue"]]]])

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
              [:input.form-control {:id "conference-input" :placeholder "relate it to the conference"}]]]]]]]])))
