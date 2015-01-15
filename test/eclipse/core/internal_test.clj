(ns eclipse.core.internal-test
  (:require [clojure.test :refer :all]
            [eclipse.core.internal :refer :all]))

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

(deftest- filter-and-return-targets-test
          (is (= 1 (count (targets-for "attacker" [def-interceptor att-interceptor]))))
          (is (= 2 (count (targets-for "defender" [def-interceptor att-interceptor att-interceptor]))))
          (is (= "defender" (:state (get (targets-for "attacker" [def-interceptor att-interceptor]) 0))))
          (is (= "attacker" (:state (get (targets-for "defender" [def-interceptor att-interceptor]) 0)))))

(deftest- check-if-ship-has-missiles-test
          (is (has-missiles? att-interceptor))
          (is (has-missiles? att-dreadnought))
          (is (has-missiles? def-cruiser))
          (is (not (has-missiles? def-interceptor))))

(deftest- get-hit-probabilities
          (is (= (/ 1 6) (hit-once-odds att-interceptor def-interceptor)))
          (is (= (/ 5 6) (hit-once-odds att-dreadnought def-interceptor)))
          (is (= (/ 4 6) (hit-once-odds att-dreadnought def-cruiser)))
          (is (= (/ 1 6) (hit-once-odds def-interceptor att-dreadnought))))

(deftest- binomial-probability
          (is (= 1 (binom 1 1)))
          (is (= 10 (binom 5 3)))
          (is (= 3003 (binom 15, 5))))
