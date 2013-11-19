(ns auqlue.layout.base
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
