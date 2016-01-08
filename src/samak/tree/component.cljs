(ns samak.tree.component
  (:require [cljs.pprint :refer [pprint]]
            [goog.events :as events]
            [revent.core :as rv])
  (:import [goog.events EventType]))

(defmulti render :type)

(defn node [{:keys [selected?]
             :as node}]
  ;(println "rendering node")
  ;(pprint node)
  [:div.node {:class (when selected? :selected)}
   (render node)])

(defmethod render :symbol [{:keys [label]}]
  ;(println "rendering symbol" label)
  [:p label])

(defmethod render :list [{:keys [children selected?]}]
  ;(println "rendering list")
  ;(pprint children)
  [:ul
   (map-indexed (fn [i child]
                  ^{:key i}
                  [:li (node child)])
                children)])

(defn tree-view [root]
  ;(println "rendering tree")
  ;(pprint root)
  [:div.tree
   (node root)])

(def keymap {37 :left
             38 :up
             39 :right
             40 :down})

(defn tree [system]
  (let [root (rv/query system :root)
        handle-key (fn [key-event]
                     (when-let [event (-> key-event
                                          .-keyCode
                                          keymap)]
                       ;(println "event:" event)
                       (rv/send system event)
                       ;(println "after" event)
                       ;; (pprint @root)
                       ))
        tree-with-handler (with-meta tree-view
                            {:component-did-mount (fn [x]
                                                    (events/listen js/window
                                                                   EventType.KEYDOWN
                                                                   handle-key))
                             :component-will-unmount (fn [x]
                                                       (events/unlisten js/window
                                                                        EventType.KEYDOWN
                                                                        handle-key))})]
    (fn []
      [tree-with-handler @root])))
