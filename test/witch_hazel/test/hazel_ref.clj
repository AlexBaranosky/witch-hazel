(ns witch-hazel.test.hazel-ref
  (:require [clojure.test :refer :all]
            [witch-hazel.hazel-ref :refer :all]
            [witch-hazel.config :refer :all]))


(def hazelcast (make-hazelcast))

(deftest all-public-vars-have-docstrings
  (is (= [] (remove (comp :doc meta) (vals (ns-publics 'witch-hazel.hazel-ref))))))

(deftest test-hazel-ref
  (let [r1 (hazel-ref hazelcast "my-ref" 1)
        _  (is (= 1 @r1))
        r2 (hazel-ref hazelcast "my-ref" 2)
        _  (is (= 2 @r1 @r2))
        _ (do-transaction hazelcast
                           (halter r1 + 3 4))
        _ (is (= 9 @r1 @r2))
        _ (do-transaction hazelcast
                          (hazel-ref-set r2 99))
        _ (is (= 99 @r1 @r2))]))

