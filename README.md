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
;; @r1 => 1

(def r2 (hazel-ref hazelcast "my-ref" 2))
;; @r1 => 2
;; @r2 => 2

(do-transaction hazelcast
                (halter r1 + 3 4))
;; @r1 => 9
;; @r2 => 9

(do-transaction hazelcast
                (hazel-ref-set r2 88))
;; @r1 => 88
;; @r2 => 88
```

`dostransaction` can roll-back

```clj
(def  r1 (hazel-ref hazelcast "my-ref1" 1))
;; @r1 => 1

(def r2 (hazel-ref hazelcast "my-ref2" 2))
;; @r2 => 2

(do-transaction hazelcast
                (halter r1 + 3 4)
                (throw (Exception. "note, no changes to r1 or r2"))
                (halter r2 +99))
;; @r1 => 1
;; @r2 => 2
```

Hazel Caches
------------


Hazel Memoization
-----------------

