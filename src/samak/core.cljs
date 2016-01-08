(ns samak.core
  (:require
   [reagent.core :as reagent]
   [revent.core :as rv])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))

(enable-console-print!)

(defcard-rg first-card
  [:div
   [:h1 "This is your first devcard!"]])

(defn main []
  ;; conditionally start the app based on wether the #main-app-area
  ;; node is on the page
  (when-let [node (.getElementById js/document "main-app-area")]
    #_(js/React.render (sab/html [:div "This is working"]) node)))

(main)

;; remember to run lein figwheel and then browse to
;; http://localhost:3449/cards.html
