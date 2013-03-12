(ns hazer.cache
  (:import (com.hazelcast.core HazelcastInstance EntryListener
                               IMap EntryEvent Transaction)
           (java.util Map)))


(defn- make-hazelcast-config [opts]
  (let [config (.build (XmlConfigBuilder.))]
    (when-let [peers (:peers opts)]
      (let [tcp-config (-> config
                           .getNetworkConfig
                           .getJoin
                           .getTcpIpConfig)]
        (doseq [peer peers]
          (.addMember tcp-config peer))))
    config))

(defn make-hazelcast [opts]
  (Hazelcast/newHazelcastInstance (make-hazelcast-config opts)))

(defn ^Map get-map [hazelcast name]
  (.getMap ^HazelcastInstance hazelcast name))

(defn add-entry-listener! [^IMap m ^EntryListener entry-listener]
  (.addEntryListener m entry-listener true))

(defn swap! [^IMap m f & args]
  (.put m (apply f (.get m) args)))

(defn reset! [^IMap m x]
  (.put m x))

(defn do-transaction* [body-fn]
  (let [^Transaction txn Hazelcast/getTransaction]
    (.begin txn)
    (try
      (body-fn)
      (.commit txn)
      (catch Throwable _
        (.rollback txn))))

(defmacro dotransaction [& body]
  `(do-transaction* (fn [] ~@body)))


(comment
  (add-entry-listener rules-cache
                    (reify EntryListener
                           (..)
                           (...)
                           (....)
                           (.....)))

  (dotransaction
   (hazer/swap! rules-cache assoc-in [:a :b] :foo))))