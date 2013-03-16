(ns hazer.memoize
  #_(:require [hazer.hazer-ref :as cache]))


#_(defn memoize [hazelcast name f]
  (let [mem (cache/get-map hazelcast name)]
    (fn [& args]
      (if-let [e (find @mem args)]
        (val e)
        (let [ret (apply f args)]
          (swap! mem assoc args ret)
          ret)))))