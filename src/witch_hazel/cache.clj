(ns witch-hazel.cache
  (:require [clojure.core.cache :as cccache]))

(cccache/defcache BasicHazelCache [cache]
  cccache/CacheProtocol
  (lookup [_ item]
    (get cache item))
  (lookup [_ item not-found]
    (get cache item not-found))
  (has? [_ item]
    (contains? cache item))
  (hit [this item] this)
  (miss [_ item result]
    (BasicHazelCache. (assoc cache item result)))
  (evict [_ key]
    (BasicHazelCache. (dissoc cache key)))
  (seed [_ base]
    (BasicHazelCache. base))
  Object
  (toString [_] (str cache)))

(defn basic-hazel-cache-factory
  ""
  [base]
  {:pre [(map? base)]}
  (BasicHazelCache. base))
