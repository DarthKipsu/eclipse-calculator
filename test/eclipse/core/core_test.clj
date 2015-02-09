(ns eclipse.core.core-test
  (:require [clojure.test :refer :all]
            [eclipse.core.core :refer :all]
            [eclipse.core.internal.test-data :refer :all])
  (:use midje.sweet))

(facts "vector reforming"
  (fact "transforms the vector received from ui to a more usable form"
        (reform-ships [json-01 json-02]) => [reform-01 reform-02]
        (reform-ships [json-int json-int2]) => [ref-int ref-int2]))

(facts "final win probabilities"
  (fact "returns a map containing win probabilities for both sides and alive odds
        for all ships in battle after missiles and 3 cannon rounds."
        (win-probabilities [cannon-04 cannon-05]) =>
          {"defender" (format "%.1f" 86.7),
           "attacker" (format "%.1f" 13.3),
           "alive-odds" [(format "%.1f" 79.8) (format "%.1f" 37.7)]}
        (win-probabilities [ref-int ref-int2]) =>
          {"defender" (format "%.1f" 54.6),
           "attacker" (format "%.1f" 45.4),
           "alive-odds" [(format "%.1f" 70.5) (format "%.1f" 66.5)]}))
