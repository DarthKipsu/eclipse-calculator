(ns eclipse.core.my-clj-function-implementations-test
  (:require [clojure.test :refer :all]
            [eclipse.core.my-clj-function-implementations :refer :all])
  (:use midje.sweet))

(facts "my get-in implementation"
  (fact "my-get-in gets a nested value from a map"
        (my-get-in {:a {:b 1}} [:a :b]) => 1
        (my-get-in {:a {:c 1} :b {:d 2}} [:a :c]) => 1
        (my-get-in {:a {:c 1} :b {:d 2}} [:b :d]) => 2
        (my-get-in {:a {:b {:c 1}}} [:a :b :c]) => 1))

(facts "my repeat implementation"
  (fact "my-repeat repeats any value given times"
        (my-repeat 0 1) => '()
        (my-repeat 1 1) => '(1)
        (my-repeat 2 1) => '(1 1)
        (my-repeat 3 {}) => '({} {} {})
        (my-repeat 4 :a) => '(:a :a :a :a)
        (my-repeat 5 ":)") => '( ":)" ":)" ":)" ":)" ":)")
        (my-repeat 20 1) => '(1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1)))

(facts "my map implementation"
  (fact "apply-all helper function returns sequences where the given function
        has been applied to all members of the given seq"
        (apply-all inc '(1 2 3 4)) => [2 3 4 5]
        (apply-all first '([1] [1 2] [1 2 3])) => [1 1 1]
        (apply-all rest '([1] [1 2] [1 2 3])) => [[] [2] [2 3]])
  (fact "my-map works like the clojure core map"
        (my-map inc '()) => []
        (my-map inc [1 2 3]) => [2 3 4]
        (my-map + [1 2 3] [4 5 6]) => [5 7 9]
        (my-map :a [{:a 1 :b 2} {:a 3 :c 4}]) => [1 3]
        (my-map list [1 2 3] '(\a \b \c) '(4 5)) => [[1 \a 4] [2 \b 5]])
  (fact "returns vectors"
        (vector? (my-map inc [1 2 3]))))

(facts "my first and rest implementations"
  (fact "to-vector returns a seq as a vector"
        (vector? (to-vector '(1))))
  (fact "my-first returns the first element in a seq."
        (my-first [1 2 3 4]) => 1
        (my-first '(1 2 3)) => 1)
  (fact "my-rest returns a vector without the first element."
        (my-rest [1 2 3 4]) => [2 3 4]
        (my-rest '(1 2 3)) => [2 3]
        (vector? (my-rest [1 2]))))

(facts "my range implementation"
  (fact "my-range returns a range starting from 0"
        (my-range 0) => '()
        (my-range 4) => '(0 1 2 3)
        (my-range 9) => '(0 1 2 3 4 5 6 7 8))
  (fact "my-range returns a range between given values"
        (my-range -1 1) => '(-1 0)
        (my-range 1 6) => '(1 2 3 4 5)
        (my-range -5 4) => '(-5 -4 -3 -2 -1 0 1 2 3)))
