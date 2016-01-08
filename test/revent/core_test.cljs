(ns revent.core-test
  (:require [cljs.test     :refer-macros [is]]
            [devcards.core :refer-macros [defcard-rg defcard-doc deftest]]
            [revent.core   :as e]))

(enable-console-print!)

(defn inc-handler [{:keys [state]}]
  (swap! state update :counter inc))

(defn inc-n-times-handler [sys n]
  (dotimes [_ n]
    (e/send sys :inc)))

(defn plus-n-handler [{:keys [state]} n]
  (swap! state update :counter + n))

(defn counter-query [state]
  (:counter @state))

(deftest send
  (let [sys (e/make-system
             :init-state {:counter 0}
             :handlers {:plus plus-n-handler
                        :inc inc-handler
                        :inc-n inc-n-times-handler})]
    (e/send sys :plus 4)
    (is (= @(:state sys)
           {:counter 4}))
    (e/send sys :inc)
    (is (= @(:state sys)
           {:counter 5}))
    (is (= @(:event-log sys)
           [[:plus 4]
            [:inc]]))
    (e/send sys :inc-n 2)
    (is (= @(:state sys)
           {:counter 7}))
    (is (= @(:event-log sys)
           [[:plus 4]
            [:inc]
            [:inc-n 2]
            [:inc]
            [:inc]]))))

(deftest query
  (let [sys (e/make-system
             :init-state {:counter 0}
             :handlers {:inc inc-handler}
             :queries {:counter counter-query})
        counter (e/query sys :counter)]
    (is (= 0 @counter))
    (e/send sys :inc)
    (is (= 1 @counter))))

(defn counter-component [sys]
  (let [counter (e/query sys :counter)]
    (fn []
      [:p "Counter: " @counter])))

(defn counter-system []
  (let [sys (e/make-system
             :init-state {:counter 0}
             :handlers {:tick inc-handler}
             :queries {:counter counter-query})
        timer (js/setInterval #(do (e/send sys :tick))
                              1000)
        wrapped-counter (with-meta
                          counter-component
                          {:component-will-unmount (fn []
                                                     (js/clearInterval timer))})]

    [wrapped-counter sys]))

(defcard-rg counter
  counter-system)
