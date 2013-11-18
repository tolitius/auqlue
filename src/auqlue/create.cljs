(ns auqlue.create
  (:require [jayq.core :as jq]
            [goog.net.XhrIo :as xhr]
            [cljs.reader :as reader])
  (:use [jayq.core :only [$]])
  (:use-macros [crate.def-macros :only [defpartial]]))

(def $fletters [($ ".create-auqlue .an")
                ($ ".create-auqlue .ul")
                ($ ".create-auqlue .qu")
                ($ ".create-auqlue .li")
                ($ ".create-auqlue .un")
                ($ ".create-auqlue .ev")])

(defn fade-out-in [xs]
  (doall (map-indexed 
    (fn [i el]
      (js/setTimeout
        #((jq/fade-out el 1000)
          (jq/slide-down el)) (* i 300)))
    xs)))

(fade-out-in $fletters)
