(ns hazer.hazel-ref
  (:import (com.hazelcast.core Hazelcast HazelcastInstance
                               EntryListener IMap EntryEvent Transaction)
           (com.hazelcast.config XmlConfigBuilder)
           (java.util Map)))


(deftype ^:private HazelRef [hazelcast hazel-map k]
         clojure.lang.IDeref
         (deref [_] (.get hazel-map k)))

(defn hazel-ref [hazelcast name x]
  (let [m (.getMap hazelcast name)
        k "value"]
    (.put m k x)
    (HazelRef. hazelcast m k)))

;; (defn hazel-ref-set [^HazelRef m x]
;;   (.put m x))

(defn do-transaction* [hazelcast body-fn]
  (let [^Transaction txn (.getTransaction hazelcast)]
    (.begin txn)
    (try
      (body-fn)
      (.commit txn)
      (catch Throwable _
        (.rollback txn)))))

(defmacro dotransaction [hazelcast & body]
  `(do-transaction* ~hazelcast (fn [] ~@body)))

(defn halter [hazel-ref* f & args]
  (let [txn-status (.. hazel-ref* hazelcast getTransaction getStatus)
        _ (assert (not (contains? #{Transaction/TXN_STATUS_NO_TXN Transaction/TXN_STATUS_UNKNOWN} txn-status))
                  "halter must be called from within a hazelcast transaction.")
        current-value (.get (.hazel-map hazel-ref*) (.k hazel-ref*))
        new-val (apply f current-value args)]
    (.put (.hazel-map hazel-ref*) (.k hazel-ref*) new-val)))

