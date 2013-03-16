(ns witch-hazel.test.cache
  (:require [witch-hazel.cache :refer :all]
            [witch-hazel.config :as config]
            [clojure.test :refer :all]
            [clojure.core.cache.tests :as t]))


(def big-map {:a 1, :b 2, :c {:d 3, :e 4}, :f nil, :g false, nil {:h 5}})
(def small-map {:a 1 :b 2})

(def hazelcast (config/make-hazelcast))

(deftest test-basic-cache-ilookup
  (testing "counts"
    (is (= 0 (count (basic-hazel-cache-factory hazelcast "name" {}))))
    (is (= 1 (count (basic-hazel-cache-factory hazelcast "name" {:a 1})))))
  
  (testing "that the BasicHazelCache can lookup via keywords"
    (t/do-ilookup-tests (basic-hazel-cache-factory hazelcast "name" small-map)))
  
  (testing "that the BasicHazelCache can .lookup"
    (t/do-dot-lookup-tests (basic-hazel-cache-factory hazelcast "name" small-map)))
  
  (testing "assoc and dissoc for BasicHazelCache"
    (t/do-assoc (basic-hazel-cache-factory hazelcast "name" {}))
    (t/do-dissoc (basic-hazel-cache-factory hazelcast "name" {:a 1 :b 2})))
  
  (testing "that get and cascading gets work for BasicHazelCache"
    (t/do-getting (basic-hazel-cache-factory hazelcast "name" big-map)))
  
  (testing "that finding works for BasicHazelCache"
    (t/do-finding (basic-hazel-cache-factory hazelcast "name" small-map)))
  
  (testing "that contains? works for BasicHazelCache"
    (t/do-contains (basic-hazel-cache-factory hazelcast "name" small-map))))
