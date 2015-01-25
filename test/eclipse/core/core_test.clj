(ns eclipse.core.core-test
  (:require [clojure.test :refer :all]
            [eclipse.core.core :refer :all]
            [eclipse.core.internal-test-data :refer :all])
  (:use midje.sweet))

(facts "vector reforming"
  (fact "transforms the vector received from ui to a more usable form"
        (reform-ships [json-01 json-02]) => [reform-01 reform-02]))
