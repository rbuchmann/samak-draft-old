(ns samak.tree.system-test
  (:require [cljs.test         :as test
                               :refer-macros [is]]
            [clojure.zip       :as z]
            [devcards.core     :as dc
                               :refer-macros [defcard-rg deftest]]
            [revent.core       :as rv]
            [samak.tree.system :as ts]))

(deftest treezip
  (is (= (-> {:children [{:a 1}]}
             ts/tree-zip
             z/down
             z/node)
         {:a 1})))

(deftest z-update
  (is (= (-> {:children [{:a 1}]}
             ts/tree-zip
             z/down
             (ts/z-update assoc :b 2)
             z/root)
         {:children [{:a 1 :b 2}]})))

(deftest root-of
  (let [sys (ts/make-tree-system {:children [{:a 1}]
                                  :type :list})
        root (ts/root-of sys)]
    (rv/send sys :down)
    (is (= @root
           {:children [{:a 1 :selected? true}]
            :type :list}))))
