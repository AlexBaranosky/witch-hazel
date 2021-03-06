(ns witch-hazel.config
  (:import (com.hazelcast.core Hazelcast HazelcastInstance)
           (com.hazelcast.config XmlConfigBuilder)))


(defn- make-hazelcast-config [peers]
  (let [config (.build (XmlConfigBuilder.))]
    (when-let [peers* peers]
      (let [tcp-config (-> config
                           .getNetworkConfig
                           .getJoin
                           .getTcpIpConfig)]
        (doseq [peer peers*]
          (.addMember tcp-config peer))))
    config))

(defn make-hazelcast
  "Make a new HazelCast instance"
  [& {:keys [peers]}]
  (Hazelcast/newHazelcastInstance (make-hazelcast-config peers)))

