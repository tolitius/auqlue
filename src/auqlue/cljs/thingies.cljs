(ns auqlue.thingies
  (:require [jayq.core :as jq])
  (:use [jayq.core :only [$]])
  (:use-macros [crate.def-macros :only [defpartial]]))

(defn fade-out-in [xs & {:keys [show f-tout i-tout]
                           :or  {show jq/slide-down
                                 f-tout 1000
                                 i-tout 300}}]
  (doall (map-indexed 
    (fn [i el]
      (js/setTimeout
        #((jq/fade-out el f-tout)
          (show el)) (* i i-tout)))
    xs)))

(defn info [& m]
  (.log js/console (apply str m)))

(defn info->js [m]
  (.log js/console (clj->js m)))
