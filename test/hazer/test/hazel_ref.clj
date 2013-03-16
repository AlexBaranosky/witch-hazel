(ns hazer.test.hazel-ref
  (:require [clojure.test :refer :all]
            [hazer.hazel-ref :refer :all]
            [hazer.config :refer :all]))


(def hazelcast (make-hazelcast))

(deftest test-hazel-ref
  (let [r1 (hazel-ref hazelcast "my-ref" 1)
        _  (is (= 1 @r1))
        r2 (hazel-ref hazelcast "my-ref" 2)
        _  (is (= 2 @r1 @r2))
        _ (do-transaction* hazelcast
                           (fn [] (halter r1 + 3 4)))
        _ (is (= 9 @r1 @r2))]))

