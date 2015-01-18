(ns eclipse.core.internal-test
  (:require [clojure.test :refer :all]
            [eclipse.core.internal :refer :all])
  (:use midje.sweet))

(def att-int
  {:state "attacker",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 1,
                :computer 0,
                :shield 0,
                :hull 1}
   :hits [36 20 4]
   :alive 1})

(def att-dre
  {:state "attacker",
   :components {:dice1HPmissile 1,
                :dice2HPmissile 0,
                :computer 5,
                :shield -5,
                :hull 5}
   :hits [1 0 0 0 0 0 0]
   :alive 1})

(def def-int
  {:state "defender",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 0,
                :computer 0,
                :shield 0,
                :hull 0}
   :hits [1 0]
   :alive 1})

(def def-cru
  {:state "defender",
   :components {:dice1HPmissile 1,
                :dice2HPmissile 2,
                :computer 2,
                :shield -2,
                :hull 1}
   :hits [36 25 0]
   :alive 1})

(facts "filter and return targets"
  (fact "attacker can find a defending ship as target"
        (count (targets-for "attacker" [def-int att-int])) => 1)
  (fact "defender can find a attacking ships as targets"
        (count (targets-for "defender" [def-int att-int att-int])) => 2 )
  (fact "the state of the attackers target is defender"
        (:state (get (targets-for
                       "attacker" [def-int att-int]) 0)) => "defender")
  (fact "the state of the defenders target is attacker"
        (:state (get (targets-for
                       "defender" [def-int att-int]) 0)) => "attacker" ))

(facts "missiles"
  (fact "has-missiles? returns true when ship has at least one missile"
        (has-missiles? att-int)
        (has-missiles? att-dre)
        (has-missiles? def-cru))
  (fact "defender interceptor does not have missiles"
        (has-missiles? def-int) => falsey)
  (fact "adds attacker missiles to defendes hits vector"
        (:hits (attack-with-missiles att-int def-int)) => [6 5]
        (:hits (attack-with-missiles def-cru def-int)) => [216 27]
        (:hits (attack-with-missiles def-cru att-int)) => [7776 540 108]))

(facts "get-hit-probabilities"
  (fact "1/6 odds when no modifiers"
        (hit-once-odds att-int def-int) => (/ 1 6))
  (fact "only 5/6 odds even with insane computer bonus"
        (hit-once-odds att-dre def-int) => (/ 5 6))
  (fact "correct odds when computer and shield is present"
        (hit-once-odds att-dre def-cru) => (/ 4 6))
  (fact "1/6 odds even against insane shield bonuses"
        (hit-once-odds def-int att-dre) => (/ 1 6)))

(def alive01 {:components {:hull 0} :hits [216 100]})
(def alive02 {:components {:hull 1} :hits [216 100 70]})
(def alive03 {:components {:hull 2} :hits [216 100 70 30]})
(def alive04 {:components {:hull 3} :hits [216 100 70 30 14]})
(def alive05 {:components {:hull 4} :hits [216 100 70 30 14 2]})


(facts "binomial-probability"
  (fact "Binomial coefficient is calculated correctly"
        (bin-coef 1 1) => 1
        (bin-coef 5 3) => 10
        (bin-coef 15, 5) => 3003)
  (fact "binomial distribution is calculated correctly"
        (bin-dist 1 1 1/6) => (roughly 1/6)
        (bin-dist 2 1 1/6) => (roughly 5/18)
        (bin-dist 5 3 2/6) => (roughly 40/243))
  (fact "odds to be still alive are returned correctly"
        (alive-odds def-int) => 1
        (alive-odds def-cru) => 25/36
        (alive-odds att-int) => 2/3
        (alive-odds alive01) => 25/54
        (alive-odds alive02) => 85/108
        (alive-odds alive03) => 25/27
        (alive-odds alive04) => 107/108
        (alive-odds alive05) => 1))
