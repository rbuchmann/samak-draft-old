(ns samak.core
  (:require
   [reagent.core :as reagent]
   [revent.core :as rv]))

(enable-console-print!)

(defn app []
  [:div.container
   [:h1 "Samak"]
   [:p "Nothing there yet. Check the "
    [:a {:href "cards.html"}
     "devcards"]
    " instead for something to see."]])

(defn main []
  (when-let [node (.getElementById js/document "main-app-area")]
    (reagent/render app node)))

(main)
