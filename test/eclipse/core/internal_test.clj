(ns eclipse.core.internal-test
  (:require [clojure.test :refer :all]
            [eclipse.core.internal :refer :all]))

(def att-interceptor
  {:state "attacker",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 1}})

(def att-dreadnought
  {:state "attacker",
   :components {:dice1HPmissile 1,
                :dice2HPmissile 0}})

(def def-interceptor
  {:state "defender",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 0}})

(def def-cruiser
  {:state "attacker",
   :components {:dice1HPmissile 1,
                :dice2HPmissile 1}})

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
