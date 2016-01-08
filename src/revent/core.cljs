(ns revent.core
  (:require [reagent.core  :as r]
            [reagent.ratom :refer-macros [reaction]]))

(enable-console-print!)

(defn make-system [& {:keys [init-state] :as system}]
  (merge system
         {:state (r/atom init-state)
          :event-log (r/atom [])}))

(defn handle [system event]
  (let [{:keys [handlers event-log]} system
        event-type (:type event)]
    (swap! event-log conj event)
    (if-let [handler (handlers event-type)]
      (handler system event)
      (if (= type :unhandled-event)
        (println "unhandled event:" (:event event))
        (handle system {:type :unhandled-event
                        :event event})))))

(defn make-event [event-type & [event-data]]
  (assoc event-data
         :type event-type))

(defn send [system event-type & [event-data]]
  (handle system (make-event event-type event-data)))

(defn query [system query]
  (let [{:keys [queries state]} system
        [key & args] query
        query-fn (key queries)]
    (if query-fn
      (reaction (apply query-fn state args))
      (println "no query fn for key:" key))))
