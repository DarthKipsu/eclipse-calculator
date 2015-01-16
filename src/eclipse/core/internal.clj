(ns eclipse.core.internal
  (:require [clojure.math.numeric-tower :as math]))

(defn- is-opponent-for? [state]
  (fn [target] (not= state (:state target))))

(defn- component [ship component]
  (get-in ship [:components component]))

(defn targets-for
  "Takes a string presentation of status and a list containing map presentations 
  of ships, filters through the list and returns those with unmatching states.
  TODO: filterv"
  [state all-ships]
  (filterv (is-opponent-for? state) all-ships))

(defn has-missiles?
  "Takes a map presentation of a ship and checks if the ship has any missile
  weaponry installed. Returns true if it has."
  [ship]
  (or
    (< 0 (component ship :dice1HPmissile))
    (< 0 (component ship :dice2HPmissile))))

(defn hit-once-odds
  "Takes two map presentations of ships and returns the odds for the first one
  of them to hit the second with one dice. Includes shield and computer improvements.
  Considers 6 always a hit and 1 always a miss as set by game rules."
  [ship-a ship-d]
  (let [numerator (+ 1 (component ship-a :computer) (component ship-d :shield))]
    (if (> numerator 1)
      (if (< numerator 6) (/ numerator 6) (/ 5 6))
      (/ 1 6))))

(defn bin-coef 
  "Takes two nonnegative integers n and k, where k is greater than or equal to n
  and returns the number of k-elements subsets of n-elements set, in other words
  their binomial coefficient."
  [n k]
  (let [factorial (fn [x] (apply * (range 1 (inc x))))]
    (/ (factorial n) (* (factorial k) (factorial (- n k))))))

(defn bin-dist
  "Takes three nonnegative integers n, k and p, where k is greater than or equal
  to n and p is a fraction. Returns binomial distribution where n is the number
  or repetitions, k is the number of desired cases and p is the probability of
  the desired case."
  [n k p]
  (let [q (- 1 p)]
    (* (bin-coef n k) (math/expt p k) (math/expt q (- n k)))))

(defn inc-hits-missiles
  "Takes two map presentations of ships, the attacking ship and its target. Returns
  the target with updated hit counter according to attacker missiles."
  [ship-a ship-d]
  (let [hp1 (component ship-a :dice1HPmissile)
        hp2 (component ship-a :dice2HPmissile)
        hits (:hits ship-d)]
    (assoc ship-d :hits (assoc hits 0 (+ (get hits 0) hp1) 1 (+ (get hits 1) hp2)))))
