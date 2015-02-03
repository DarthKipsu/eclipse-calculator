(ns eclipse.core.core-test
  (:require [clojure.test :refer :all]
            [eclipse.core.core :refer :all]
            [eclipse.core.internal.test-data :refer :all])
  (:use midje.sweet))

(facts "vector reforming"
  (fact "transforms the vector received from ui to a more usable form"
        (reform-ships [json-01 json-02]) => [reform-01 reform-02]))

(facts "final win probabilities"
  (fact "returns a map containing win probabilities for both sides and alive odds
        for all ships in battle after missiles and 3 cannon rounds."
        (win-probabilities [cannon-04 cannon-05]) =>
          {"defender" "81,6",
           "attacker" "18,4",
           "alive-odds" ["73,2" "38,1"]}))
