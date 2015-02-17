(ns eclipse.core.internal.odds
  (:require [clojure.math.numeric-tower :as math]
            [eclipse.core.internal.my-clj-function-implementations :refer :all])
  (:use [eclipse.core.internal.reforms]))

(defn component [ship component]
  (my-get-in ship [:components component]))

(defn- has-no-hits? [ship]
  (let [hits (:hits ship)]
    (= 1 (hits 0))))

(defn hull-hit-combinations
  "Takes the amount of hull as an integer and a vector containing hit combina-
  tions and returns the number of combinations where the ship has not yet
  received a fatal amount of hits."
  [hull hits]
  (let [indexes (my-range 1 (+ 2 hull))]
    (apply + (my-map hits indexes))))

(defn alive-odds
  "Takes a map presentation of a ship and returns the odds the ship is still
  alive as a ratio."
  [ship]
  (let [hits (:hits ship)
        all-combinations (hits 0)
        hull (component ship :hull)]
    (if (has-no-hits? ship) 1
      (/ (hull-hit-combinations hull hits) all-combinations))))

(defn hit-once-odds
  "Takes two map presentations of ships and returns the odds for the first one
  of them to hit the second with one dice. Includes shield and computer improve-
  ments. Considers 6 always a hit and 1 always a miss as set by game rules. Takes
  previous hits into consideration by multiplying the oss with odds to be alive."
  [ship-a ship-d]
  (let [prev-odds (* (alive-odds ship-d) (alive-odds ship-a))
        numerator (+ 1 (component ship-a :computer) (component ship-d :shield))]
    (if (> numerator 1)
      (if (< numerator 6) (* prev-odds (/ numerator 6)) (* prev-odds(/ 5 6)))
      (* prev-odds (/ 1 6)))))

(defn bin-coef 
  "Takes two nonnegative integers n and k, where k is greater than or equal to n
  and returns the number of k-elements subsets of n-elements set, in other words
  their binomial coefficient."
  [n k]
  (let [factorial (fn [x] (apply * (my-range 1 (inc x))))]
    (/ (factorial n) (* (factorial k) (factorial (- n k))))))

(defn bin-dist
  "Takes three nonnegative integers n, k and p, where k is greater than or equal
  to n and p is a fraction. Returns binomial distribution where n is the number
  or repetitions, k is the number of desired cases and p is the probability of
  the desired case."
  [n k p]
  (let [q (- 1 p)]
    (* (bin-coef n k) (math/expt p k) (math/expt q (- n k)))))

(defn dice-hit-freq
  "Takes a dice damage as an integer and hit odds as a ratio and returns the
  frequency of each damage as a map."
  [damage odds]
  (let [damage-n (numerator odds)]
    {damage damage-n, 0 (- (denominator odds) damage-n)}))

(defn combination 
  "Takes an array of previous hits, the index of the array to update, a map
  containing frequencies for hit and miss and returns the updated combination
  value for the received index."
  [hits i freq]
  (let [damage (my-some #(if (pos? %) %) (keys freq))]
    (if (<= i damage) (if (zero? (get hits i)) (if (= 1 i) (freq 0) 0)
                        (* (get hits i) (freq 0)))
      (if (zero? (hits 1)) (if (= (inc damage) i) (freq damage) 0)
        (+ (* (freq damage) (get hits (- i damage))) (* (hits i) (freq 0)))))))

(defn add-combinations
  "Takes previous hits as a vector and a map containing hit frequencies for a 
  single weapon hit. Returns a new hits vector containing updated hit combinations."
  [hits freq]
  (let [damage (my-some #(if (pos? %) %) (keys freq))
        hull (count hits)]
    (loop [acc [(* (apply + (vals freq)) (hits 0))] i 1]
      (if (>= i hull) acc
        (recur (conj acc (combination hits i freq)) (inc i))))))

(defn clipped
  "Takes an integer and returns a smaller integer with last n digits clipped off."
  [value n]
  (let [multiplier (/ 1 (math/expt 10 n))]
    (math/round (* value multiplier))))

(defn clip-value
  "Takes an integer and checks how much it needs to be rounded down to keep the
  value in reasonable size. Returns the number of digits that need to be clipped
  from the end of the number."
  [value]
  (count (str (math/round (/ value 1E10)))))

(defn clipped-vec
  "takes a vector containing integers and clips each value from the end enough
  digits for the first value to be less than 1E11. Returns a new vector with 
  updated values."
  [a-vec]
  (let [n (clip-value (my-first a-vec))]
    (mapv (fn [value] (clipped value n)) a-vec)))

(defn all-weapon-combinations
  "Takes previous hits as vector, the number of weapons and damage value for those
  weapons, as well as odds for hitting the target and returns a vector containing
  combinations of all hits produced by those weapons, added on top of the combi-
  nations of any previous hits."
  [hits weapons damage odds]
  (loop [acc hits i weapons]
    (if (zero? i)
      (if (< 1E10 (my-first acc)) (clipped-vec acc) acc)
      (recur (add-combinations acc (dice-hit-freq damage odds)) (dec i)))))

(defn alive-odds-formatted
  "Takes a vector containing map presentations of ships and returns another 
  vector containing formatted alive odds for each ship."
  [ships]
  (my-map (fn [ship] (format "%.1f" (* 100 (double (:alive ship))))) ships))

(defn none-alive-odds
  "Takes a vector containing map presentations of ships and returns the odds for 
  none of the ships to be alive as a double"
  [ships]
  (my-reduce (fn [acc ship] (* acc (- 1 (double (:alive ship))))) 1 ships))

(defn opponent-destroyed
  "Takes the odds that none of the players ships are alive and the odds for none 
  of the opponent ships to be alive as doubles and returns the odds at least one
  of the player ships survived and none of the opponent ships did."
  [n-a-odds-a n-a-odds-b]
  (* (- 1 n-a-odds-a) n-a-odds-b))

(defn win-odds-defender
  "Takes a vector containing map presentations for both the defender and attacker
  and checks all ship alive odds and based on them returns the odds for the 
  defenders side to win as a double"
  [d-ships a-ships]
  (let [d-none-alive (none-alive-odds d-ships)
        a-none-alive (none-alive-odds a-ships)
        a-destroyed (opponent-destroyed d-none-alive a-none-alive)
        d-destroyed (opponent-destroyed a-none-alive d-none-alive)]
    (if (zero? a-none-alive)
      0.0 
      (/ a-destroyed (+ a-destroyed d-destroyed)))))

(defn win-odds-attacker
  "Takes the odds for the defender to win and a vector containing defender ships
  as a parameter and returns the odds the attacker will win ass a double."
  [d-win-odds d-ships]
  (if (and (zero? d-win-odds) (zero? (none-alive-odds d-ships)))
    0.0
    (- 1 d-win-odds)))
