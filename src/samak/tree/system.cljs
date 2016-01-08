(ns samak.tree.system
  (:require [clojure.zip :as z]
            [revent.core :as rv]))

(defn tree-zip [root]
  (z/zipper :children
            (comp seq :children)
            (fn [node children]
              (assoc node :children children))
            root))

(defn nil-proof-handler [move]
  (let [maybe-move (fn [loc]
                     (or (move loc) loc))]
    (fn [system]
      (swap! (:state system) maybe-move))))

(def tree-mov-handlers
  (->> [z/up z/down z/left z/right]
       (map nil-proof-handler)
       (zipmap [:up :down :left :right])))

(defn z-update [loc f & args]
  (z/replace loc
             (apply f (z/node loc) args)))

(defn select-and-root [loc & _]
  (-> @loc
      (z-update assoc :selected? true)
      z/root))

(defn make-tree-system [root]
  (rv/make-system :init-state (tree-zip root)
                  :handlers tree-mov-handlers
                  :queries {:root select-and-root}))

(defn root-of [system]
  (rv/query system :root))
