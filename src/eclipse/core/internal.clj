(ns eclipse.core.internal
  (:require [clojure.math.numeric-tower :as math]))

(defn- is-opponent-for? [state]
  (fn [target] (not= state (:state target))))

(defn- component [ship component]
  (get-in ship [:components component]))

(defn- has-no-hits? [ship]
  (let [hits (:hits ship)]
    (and (empty? (:1 hits)) (empty? (:2 hits)) (empty? (:4 hits)))))

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

(defn hits-with-attacks
  "Takes map containing current hits, attacker weapon count and odds for a single
  hit as parameters and returns a new map containing the added hits."
  [hits weapons odds]
  (if (> weapons 0)
    (assoc hits odds (if (get hits odds)
                       (+ (get hits odds) weapons)
                       weapons))
    hits))

(defn attack-with-missiles
  "Takes two map presentations of ships, the attacking ship and it's target. Returns
  the target with updated hit counter according to attacker missiles."
  [ship-a ship-d]
  (let [hp1 (component ship-a :dice1HPmissile)
        hp2 (component ship-a :dice2HPmissile)
        odds (hit-once-odds ship-a ship-d)
        hits (:hits ship-d)
        hp1-new (hits-with-attacks (:1 hits) hp1 odds)
        hp2-new (hits-with-attacks (:2 hits) hp2 odds)]
    (assoc ship-d :hits (assoc hits :1 hp1-new :2 hp2-new))))

(defn hit-odds-for-hit-type
  "Takes a map containing all hits of a single weapon type (for example 1HP weapons)
  and a number k indicating the desired amount of hits the ship has taken, for ex-
  ample 0 if no desired amoutn of hits is zero. Returns the odds for the desired
  amount of hits as a float."
  [hits k]
  (let [odds (reduce (fn [acc [p n]] (* acc (bin-dist n k p))) 1 hits)]
    (if (== odds 1) 0 odds)))

(defn- alive-multiplier [& odds]
  (apply * (filter (complement zero?) odds)))

(defn alive-odds
  "Takes a map presentation of a ship, calculates the odds for the ship to be still
  alive and returns it as a float."
  [ship]
  (let [hits (:hits ship)
        hull (component ship :hull)]
    (cond
      (has-no-hits? ship) 1
      (= 0 hull) (alive-multiplier (hit-odds-for-hit-type (:1 hits) 0)
                                   (hit-odds-for-hit-type (:2 hits) 0)
                                   (hit-odds-for-hit-type (:4 hits) 0))
      (= 1 hull) (alive-multiplier (+ (hit-odds-for-hit-type (:1 hits) 0)
                                      (hit-odds-for-hit-type (:1 hits) 1))
                                   (hit-odds-for-hit-type (:2 hits) 0)
                                   (hit-odds-for-hit-type (:4 hits) 0)))))
