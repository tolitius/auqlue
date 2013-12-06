(ns auqlue.create
  (:require [auqlue.thingies :refer [fade-out-in info info->js]]
            [jayq.core :as jq]
            [cljs.reader :as reader])
  (:use [jayq.core :only [$]])
  (:use-macros [crate.def-macros :only [defpartial]]))


(def $c-header ($ :.c-header))
(def $create-auqlue ($ :.create-auqlue))
(def $prezi-input ($ :#prezi-input))
(def $aa-form ($ :.aa-form))

(def $presenter-input ($ :#presenter-input))
(def $event-input ($ :#conference-input))
(def $add-presenter-input ($ :#add-presenter-input))
(def $add-event-input ($ :#add-event-input))

(def $new-auqlue-btn ($ :#new-auqlue))
(def $add-auqlue-form ($ :.add-auqlue))

(def $optional ($ :.o-footer))
(def $prezi-name ($ :.prezi-name))
(def $auqlue-info ($ :.auqlue-info))
(def $create-btn ($ :.create-btn-div))

(def $fletters [($ ".create-auqlue .an")
                ($ ".create-auqlue .ul")
                ($ ".create-auqlue .qu")
                ($ ".create-auqlue .li")
                ($ ".create-auqlue .un")
                ($ ".create-auqlue .ev")])

(fade-out-in $fletters)

(defpartial auqlue-link [details]
 [:a {"href" details}
   [:div.alert.alert-warning.auqlue-link 
     (str "auqlue.com" details)]])

(defn show-details [data]
  (let [details (-> (reader/read-string data)
                    clj->js)]
    (jq/hide $optional)
    (jq/hide $c-header)
    (jq/hide $create-btn)
    (jq/html $prezi-name "Auqlue is Ready")
    (jq/hide $auqlue-info)
    (jq/css $prezi-name {:color "rgba(163, 91, 68, 0.73)"})
    (jq/css $create-auqlue {:margin-top "10%"})
    (jq/attr $aa-form :class "form-group col-md-10 col-md-offset-1")
    (jq/html $auqlue-info (auqlue-link details))
    (.css (jq/fade-in $auqlue-info "slow") "display" "block")))

(defn save-new-auqlue [auqlue]
  (jq/ajax {:url "/new-auqlue"
            :data auqlue
            :type "POST"
            :success show-details}))

(defn create-auqlue []
  (let [prezi (.val $prezi-input)
        presenter (.val $presenter-input)
        event (.val $event-input)]
    (.log js/console "prezi: " prezi ", presenter: " presenter ", event: " event)
    (if (seq prezi)                                                                ;; add validation with "alerts"
      (save-new-auqlue {:prezi prezi :presenter presenter :event event})
      (.log js/console "please enter \"Presentation Name\""))))

(jq/on $new-auqlue-btn :click
  (fn [e]
    (.preventDefault e)
    (create-auqlue)))

(jq/on $presenter-input :keydown
  (fn [e]
    (if (= (.-keyCode e) 13)
      (do
        (.preventDefault e)
        (.click $new-auqlue-btn)))))

(jq/on $event-input :keydown
  (fn [e]
    (if (= (.-keyCode e) 13)
      (do
        (.preventDefault e)
        (.click $new-auqlue-btn)))))
