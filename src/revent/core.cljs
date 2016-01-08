(ns revent.core
  (:require [reagent.core  :as r]
            [reagent.ratom :refer-macros [reaction]]))

(enable-console-print!)

(defn make-system [& {:keys [init-state] :as system}]
  (merge system
         {:state (r/atom init-state)
          :event-log (r/atom [])}))

(defn send [system event-type & args]
  (let [{:keys [handlers event-log]} system
        event (apply vector event-type args)]
    (swap! event-log conj event)
    (if-let [handler (handlers event-type)]
      (apply handler system args)
      (if (= event-type :unhandled-event)
        (println "unhandled event:" (:event event))
        (send system :unhandled-event event)))))

(defn query [system query-key & args]
  (let [{:keys [queries state]} system
        query-fn (query-key queries)]
    (if query-fn
      (reaction (apply query-fn state args))
      (println "no query fn for key:" key))))
