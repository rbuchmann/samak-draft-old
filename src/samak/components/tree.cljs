(ns samak.components.tree
  (:require
   [reagent.core :as reagent]
   [reagent.ratom :refer-macros [reaction]]
   [revent.core :as rv]
   [clojure.zip :as z])
  (:require-macros
   [devcards.core :as dc :refer [defcard-rg deftest]]))


(defn make-tree-system []
  (make-system {:init-state (z/seq-zip ())
                :handlers {}
                :queries {:root z/root}}))

(defn root-of [system]
  (rv/query system :root))

(defn tree [system]
  (let [root (root-of system)]
    [])
  )

(defcard-rg tree-card
  )
