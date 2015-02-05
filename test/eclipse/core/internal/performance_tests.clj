(ns eclipse.core.internal.performance-tests
  (:require [clojure.test :refer :all]
            [eclipse.core.internal.my-clj-function-implementations :refer :all])
  (:use midje.sweet))

(def map1E3 (into [] (range 1000)))
(def map1E3inc (into [] (range 1 1001)))
(def map1E4 (into [] (range 10000)))
(def map1E4inc (into [] (range 1 10001)))
(def map1E6 (into [] (range 1000000)))
(def map1E6inc (into [] (range 1 1000001)))

(facts "performance of my map function"
  (fact "comparisons between my-map and mapv"
        (println "  Map, 5 values")
        (time (mapv inc [1 2 3 4 5])) => [2 3 4 5 6]
        (time (my-map inc [1 2 3 4 5])) => [2 3 4 5 6]
        (println "  Map, 1000 values")
        (time (mapv inc map1E3)) => map1E3inc
        (time (my-map inc map1E3)) => map1E3inc
        (println "  Map, 10 000 values")
        (time (mapv inc map1E4)) => map1E4inc
        (time (my-map inc map1E4)) => map1E4inc
        (println "  Map, 1 000 000 values")
        (time (mapv inc map1E6)) => map1E6inc
        (time (my-map inc map1E6)) => map1E6inc))

(def some1E3 (assoc (into [] (repeat 1000 1)) 999 2))
(def some1E4 (assoc (into [] (repeat 10000 1)) 9999 2))
(def some1E6 (assoc (into [] (repeat 1000000 1)) 999999 2))

(facts "performance of my some function"
  (fact "comparison between my-som and some"
        (println "\n  Some, 5 values")
        (time (some even? [1 3 5 7 8]))
        (time (my-some even? [1 3 5 7 8]))
        (println "  Some, 1000 values")
        (time (some even? some1E3))
        (time (my-some even? some1E3))))

