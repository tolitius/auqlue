(ns auqlue.create
  (:require [auqlue.thingies :refer [fade-out-in]]
            [jayq.core :as jq]
            [cljs.reader :as reader])
  (:use [jayq.core :only [$]])
  (:use-macros [crate.def-macros :only [defpartial]]))


(def $prezi-input ($ :#prezi-input))

(def $presenter-input ($ :#presenter-input))
(def $event-input ($ :#conference-input))
(def $add-presenter-input ($ :#add-presenter-input))
(def $add-event-input ($ :#add-event-input))

(def $new-auqlue-btn ($ :#new-auqlue))
(def $add-auqlue-form ($ :.add-auqlue))

(def $purchase-list (jq/$ :.purchase-list))
(def $status-dropdown (jq/$ :#combosel_chzn))

(def $fletters [($ ".create-auqlue .an")
                ($ ".create-auqlue .ul")
                ($ ".create-auqlue .qu")
                ($ ".create-auqlue .li")
                ($ ".create-auqlue .un")
                ($ ".create-auqlue .ev")])

(fade-out-in $fletters)

(defn show-details [data]
  (let [details (reader/read-string data)]
    (.log js/console "details: " details)))

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
