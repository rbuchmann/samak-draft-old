(ns samak.tree.component
  (:require [revent.core :as rv]))

(defmulti render :type)

(defn node [{:keys [selected?]
             :as node}]
  [:div.node {:class (when selected? :selected)}
   (render node)])

(defmethod render :symbol [{:keys [label]}]
  [:p label])

(defmethod render :list [{:keys [children selected?]}]
  [:ul
   (for [child children]
     [:li (node child)])])

(defn tree [system]
  (let [root (rv/query system :root)]
    (fn []
      (node @root))))
