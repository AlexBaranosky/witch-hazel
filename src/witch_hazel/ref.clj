(ns witch-hazel.ref
  (:import (com.hazelcast.core Hazelcast HazelcastInstance
                               EntryListener IMap Transaction)
           (com.hazelcast.config XmlConfigBuilder)
           (java.util Map)))


(deftype ^:private HazelRef [^HazelcastInstance hazelcast ^IMap hazel-map k]
         clojure.lang.IDeref
         (deref [_] (.get hazel-map k)))

(defn hazel-ref
  "TODO - add great doc string"
  [^HazelcastInstance hazelcast name x]
  (let [m (.getMap hazelcast name)
        k "value"]
    (.put m k x)
    (HazelRef. hazelcast m k)))

(defn- assert-in-transaction [^HazelRef hazel-ref*]
  (let [hazelcast ^HazelcastInstance (.hazelcast hazel-ref*)
        txn ^Transaction (.getTransaction hazelcast)
        txn-status (.getStatus txn)]
    (assert (not (contains? #{Transaction/TXN_STATUS_NO_TXN Transaction/TXN_STATUS_UNKNOWN} txn-status))
            "Must be called from within a hazelcast transaction.")))

(defn- set-hazel-ref-internal-value [^HazelRef hazel-ref* x]
  (.put ^IMap (.hazel-map hazel-ref*) (.k hazel-ref*) x))

(defn hazel-ref-set
  "TODO - add great doc string"
  [^HazelRef hazel-ref* x]
  (assert-in-transaction hazel-ref*)
  (set-hazel-ref-internal-value hazel-ref* x))

(defn do-transaction*
  "TODO - add great doc string"
  [^HazelcastInstance hazelcast body-fn]
  (let [^Transaction txn (.getTransaction hazelcast)]
    (.begin txn)
    (try
      (body-fn)
      (.commit txn)
      (catch Throwable _
        (.rollback txn)))))

(defmacro do-transaction
  "TODO - add great doc string"
  [hazelcast & body]
  `(do-transaction* ~hazelcast (fn [] ~@body)))

(defn halter
  "TODO - add great doc string"
  [^HazelRef hazel-ref* f & args]
  (assert-in-transaction hazel-ref*)
  (let [new-val (apply f @hazel-ref* args)]
    (set-hazel-ref-internal-value hazel-ref* new-val)))
