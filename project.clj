(defproject org.clojars.alexbaranosky/witch-hazel "0.0.1"
  :description "Higher-level abstraction built on Hazelcast"
  :profiles {:dev {:dependencies [[print-foo "0.3.2"]]}
             :1.4.0 {:dependencies [[org.clojure/clojure "1.4.0"]]}
             :1.5.1 {:dependencies [[org.clojure/clojure "1.5.1"]]}}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/core.cache "0.6.3"]
                 [org.clojure/core.memoize "0.5.2"]
                 [com.hazelcast/hazelcast "2.4"]]
  :aliases {"all-tests" ["with-profile" "1.4.0:1.5.1" "test"]})
