(ns eclipse.core.internal-test
  (:require [clojure.test :refer :all]
            [eclipse.core.internal :refer :all])
  (:use midje.sweet))

(def att-interceptor
  {:state "attacker",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 1,
                :computer 0,
                :shield 0,
                :hull 1}
   :hits [1 0 0]})

(def att-dreadnought
  {:state "attacker",
   :components {:dice1HPmissile 1,
                :dice2HPmissile 0,
                :computer 5,
                :shield -5,
                :hull 5}
   :hits [1 0 0 0 0 0 0]})

(def def-interceptor
  {:state "defender",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 0,
                :computer 0,
                :shield 0,
                :hull 0}
   :hits [1 0]})

(def def-cruiser
  {:state "attacker",
   :components {:dice1HPmissile 1,
                :dice2HPmissile 2,
                :computer 2,
                :shield -2,
                :hull 1}
   :hits [1 0 0]})

(facts "filter-and-return-targets-test"
  (fact "attacker can find a defending ship as target"
        (count (targets-for "attacker" [def-interceptor att-interceptor])) => 1)
  (fact "defender can find a attacking ships as targets"
        (count (targets-for
                 "defender"
                 [def-interceptor att-interceptor att-interceptor])) => 2 )
  (fact "the state of the attackers target is defender"
        (:state (get (targets-for
                       "attacker"
                       [def-interceptor att-interceptor]) 0)) => "defender")
  (fact "the state of the defenders target is attacker"
        (:state (get (targets-for
                       "defender"
                       [def-interceptor att-interceptor]) 0)) => "attacker" ))

(facts "missiles"
  (fact "attacker interceptor has missiles"
        (has-missiles? att-interceptor))
  (fact "attacker dreadnought has missiles"
        (has-missiles? att-dreadnought))
  (fact "defender cruiser has missiles"
        (has-missiles? def-cruiser))
  (fact "defender interceptor does not have missiles"
        (has-missiles? def-interceptor) => falsey))

(facts "get-hit-probabilities"
  (fact "1/6 odds when no modifiers"
        (hit-once-odds att-interceptor def-interceptor) => (/ 1 6))
  (fact "only 5/6 odds even with insane computer bonus"
        (hit-once-odds att-dreadnought def-interceptor) => (/ 5 6))
  (fact "correct odds when computer and shield is present"
        (hit-once-odds att-dreadnought def-cruiser) => (/ 4 6))
  (fact "1/6 odds even against insane shield bonuses"
        (hit-once-odds def-interceptor att-dreadnought) => (/ 1 6)))

(facts "binomial-probability"
  (fact "Binomial coefficient of 1 choose 1 is 1"
        (bin-coef 1 1) => 1)
  (fact "Binomial coefficient of 5 choose 3 is 10"
        (bin-coef 5 3) => 10)
  (fact "Binomial coefficient of 15 choose 5 is 3003"
        (bin-coef 15, 5) => 3003)
  (fact "binomial distribution when n=1, k=1 and p=1/6 is 1/6"
        (bin-dist 1 1 1/6) => (roughly 1/6))
  (fact "binomial distribution when n=2, k=1 and p=1/6 is 5/18"
        (bin-dist 2 1 1/6) => (roughly 5/18))
  (fact "binomial distribution when n=5, k=3 and p=2/6 is 40/243"
        (bin-dist 5 3 2/6) => (roughly 40/243)))
