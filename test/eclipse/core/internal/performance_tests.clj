(ns eclipse.core.internal.performance-tests
  (:require [clojure.test :refer :all]
            [eclipse.core.internal.my-clj-function-implementations :refer :all])
  (:use midje.sweet))

(def vec1000 (into [] (range 1000)))
(def vec1000inc (into [] (range 1 1001)))
(def vec1E4 (into [] (range 10000)))
(def vec1E4inc (into [] (range 1 10001)))

(facts "performance of my map function"
  (fact "comparisons between my-map and mapv"
        (println "My map, 5 values")
        (time (mapv inc [1 2 3 4 5])) => [2 3 4 5 6]
        (time (my-map inc [1 2 3 4 5])) => [2 3 4 5 6]
        (println "My map, 1000 values")
        (time (mapv inc vec1000)) => vec1000inc
        (time (my-map inc vec1000)) => vec1000inc
        (println "My map, 10 000 values")
        (time (mapv inc vec1E4)) => vec1E4inc
        (time (my-map inc vec1E4)) => vec1E4inc))
