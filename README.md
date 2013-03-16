A Set Of Higher-Level Abstractions On Top of HazelCast For Clojure
==================================================================

```clj

;; Not deployed to Clojars yet.....
[org.clojars.alexbaranosky/witch-hazel "0.0.1"]
```

Hazel Refs
----------

`hazel-ref` behaves like clojure.core/ref, but using Hazelcast as a distributed mutable state.

```clj
(def hazelcast (make-hazelcast))

(def r1 (hazel-ref hazelcast "my-ref" 1))
(is (= 1 @r1))

(def r2 (hazel-ref hazelcast "my-ref" 2))
(is (= 2 @r1 @r2))

(do-transaction hazelcast
                (halter r1 + 3 4))
(is (= 9 @r1 @r2))

(do-transaction hazelcast
                (hazel-ref-set r2 99))
(is (= 99 @r1 @r2))
```

Hazel Caches
------------


Hazel Memoization
-----------------

