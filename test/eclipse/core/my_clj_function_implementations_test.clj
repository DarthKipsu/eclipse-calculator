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
