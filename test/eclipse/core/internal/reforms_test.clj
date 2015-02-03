(ns eclipse.core.internal.reforms-test
  (:require [clojure.test :refer :all]
            [eclipse.core.internal.reforms :refer :all]
            [eclipse.core.internal.test-data :refer :all])
  (:use midje.sweet))

(facts "vector reforming helpers"
  (fact "creates hit vectors of correct size"
        (empty-hits-vector 0) => [1 0]
        (empty-hits-vector 1) => [1 0 0]
        (empty-hits-vector 2) => [1 0 0 0]
        (empty-hits-vector 3) => [1 0 0 0 0])
  (fact "turns a json originated vector to an usable form"
        (reform-single-ship 0 json-01) => reform-01
        (reform-single-ship 1 json-02) => reform-02))
