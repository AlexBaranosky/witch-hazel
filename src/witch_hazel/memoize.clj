(ns witch-hazel.memoize
  (:require [clojure.core.memoize :as ccmemoize]
            [witch-hazel.cache :as whcache])
  (:import (clojure.core.memoize PluggableMemoization)))


(defn memo
  "TODO: add great doc string"
  ([f hazelcast name]
     (memo f hazelcast name {}))
  ([f hazelcast name seed]
     (ccmemoize/build-memoizer
       #(PluggableMemoization. %1 (whcache/basic-hazel-cache-factory hazelcast name %2))
       f
       seed)))
