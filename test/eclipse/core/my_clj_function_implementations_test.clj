(ns eclipse.core.my-clj-function-implementations-test
  (:require [clojure.test :refer :all]
            [eclipse.core.my-clj-function-implementations :refer :all])
  (:use midje.sweet))

(facts "My get-in implementation"
  (fact "my-get-in gets a nested value from a map"
        (my-get-in {:a {:b 1}} [:a :b]) => 1
        (my-get-in {:a {:c 1} :b {:d 2}} [:a :c]) => 1
        (my-get-in {:a {:c 1} :b {:d 2}} [:b :d]) => 2
        (my-get-in {:a {:b {:c 1}}} [:a :b :c]) => 1))
