(ns samak.tree.component-test
  (:require
   [reagent.core :as re]
   [devcards.core :as dc :refer-macros [defcard-rg deftest]]
   [samak.tree.component :as tc]))

(defcard-rg symbol
  (tc/node {:type :symbol
            :label "bar"}))

(defcard-rg selected-symbol
  (tc/node {:type :symbol
            :selected? true
            :label "bar"}))
