(ns eclipse.core.internal.performance-tests
  (:require [clojure.test :refer :all]
            [eclipse.core.internal.my-clj-function-implementations :refer :all])
  (:use midje.sweet))

(def some1E3 (assoc (into [] (repeat 1000 1)) 999 2))
(def some1E4 (assoc (into [] (repeat 10000 1)) 9999 2))
(def some1E6 (assoc (into [] (repeat 1000000 1)) 999999 2))

(facts "performance of my map function"
  (fact "comparisons between my-map and mapv"
        (println "  Map, 5 values")
        (time (mapv inc [1 2 3 4 5]))
        (time (my-map inc [1 2 3 4 5]))
        (println "  Map, 1000 values")
        (time (mapv inc some1E3))
        (time (my-map inc some1E3))
        (println "  Map, 10 000 values")
        (time (mapv inc some1E4))
        (time (my-map inc some1E4))
        (println "  Map, 1 000 000 values")
        (time (mapv inc some1E6))
        (time (my-map inc some1E6))))

(facts "performance of my some function"
  (fact "comparison between my-some and some"
        (println "\n  Some, 5 values")
        (time (some even? [1 3 5 7 8]))
        (time (my-some even? [1 3 5 7 8]))
        (println "  Some, 1000 values")
        (time (some even? some1E3))
        (time (my-some even? some1E3))
        (println "  Some, 10 000 values")
        (time (some even? some1E4))
        (time (my-some even? some1E4))
        (println "  Some, 1 000 000 values")
        (time (some even? some1E6))
        (time (my-some even? some1E6))))
