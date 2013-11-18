(ns auqlue.views  
  (:require
    [hiccup
      [page :refer [html5 include-js include-css]]]))

(defn with-css []
  (list
   (include-css "/bootstrap/css/bootstrap.css")
   (include-css "/bootstrap/css/bootstrap-theme.css")
   (include-css "/css/auqlue.css")))

(defn with-js []
  (list
   (include-js "/js/jquery-1.10.2.min.js")
   (include-js "/bootstrap/js/bootstrap.js")
   (include-js "/js/auqlue.js")))

(defn with-bootstrap [title clazz & content]
  (html5
    [:head
      [:title title]
      [:meta {"name" "viewport" "content" "width=device-width, initial-scale=1.0"}]
      (with-css)]
    [:body
      [:div {"class" (str "container " clazz)} content]
      (with-js)]))

(defn create-auqlue []
  (with-bootstrap "Answer to the Ultimate Question of Life, the Universe, and Everything"
                  "create-auqlue"
    [:row.center
     [:p.a-header.auqlue-color "auqlue"]]
    [:row.center
     [:h5.create-auqlue 
          [:span.an.auqlue-color "A"] "nswer to the "
          [:span.ul.auqlue-color "U"] "ltimate "
          [:span.qu.auqlue-color "Q"] "uestion of "
          [:span.li.auqlue-color "L"] "ife, the "
          [:span.un.auqlue-color "U"] "niverse, and "
          [:span.ev.auqlue-color "E"] "verything"]]))
