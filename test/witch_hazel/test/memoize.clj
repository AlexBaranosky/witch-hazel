(ns witch-hazel.test.memoize
  (:require [clojure.test :refer :all]
            [clojure.core.memoize :refer [memoized? snapshot memo-clear!]]
            [witch-hazel.config :as c]
            [witch-hazel.memoize :refer :all]))


(defn test-type-transparency [factory]
  (let [mine (factory identity)
        them (memoize identity)]
    (testing "That the memo function works the same as core.memoize"
      (are [x y] =
           (mine 42) (them 42)
           (mine ()) (them ())
           (mine []) (them [])
           (mine #{}) (them #{})
           (mine {}) (them {})
           (mine nil) (them nil)))
    (testing "That the memo function has a proper cache"
      (is (memoized? mine))
      (is (not (memoized? them)))
      (is (= 42 (mine 42)))
      (is (not (empty? (snapshot mine))))
      (is (memo-clear! mine))
      (is (empty? (snapshot mine))))))


(def hazelcast (c/make-hazelcast))

(deftest asdf
  (test-type-transparency #(memo % hazelcast "func")))
