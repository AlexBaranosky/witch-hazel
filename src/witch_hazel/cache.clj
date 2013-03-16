(ns witch-hazel.cache
  (:require [clojure.core.cache :as cccache])
  (:import (com.hazelcast.core HazelcastInstance
                                IMap Transaction)))


(declare basic-hazel-cache-factory)

(cccache/defcache BasicHazelCache [cache hazelcast name ^IMap k]
  cccache/CacheProtocol
  (lookup [_ item]
          (get cache item))
  (lookup [_ item not-found]
          (get cache item not-found))
  (has? [_ item]
        (contains? cache item))
  (hit [this item] this)
  (miss [_ item result]
        (basic-hazel-cache-factory hazelcast name (assoc cache item result)))
  (evict [_ key]
         (basic-hazel-cache-factory hazelcast name (dissoc cache key)))
  (seed [_ base]
        (basic-hazel-cache-factory hazelcast name base))
  Object
  (toString [_] (str cache)))

(defn basic-hazel-cache-factory
  "TODO: add great doc string"
  [^HazelcastInstance hazelcast name base]
  {:pre [(map? base)]}
  (let [m (.getMap hazelcast name)
        k "value"]
    (.put m k base)
    (BasicHazelCache. (.get m k)  hazelcast name "value")))
