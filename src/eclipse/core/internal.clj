(ns eclipse.core.internal
  (:require [clojure.math.numeric-tower :as math]))

(defn- is-opponent-for? [state, target]
  (not= state (:state target)))

(defn- component [ship component]
  (get-in ship [:components component]))

(defn- has-no-hits? [ship]
  (let [hits (:hits ship)]
    (= 1 (get hits 0))))


(defn empty-hits-vector 
  "takes a hull count and returns an empty hits vector with enough slots for 
  each hull"
  [hull]
  (apply conj [1] (repeat (inc hull) 0)))

(defn reform-single-ship
  "Takes a vector containing a ship presentation returned by the ui and returns
  a new ship map structure created based on the input."
  [i ship-json]
  (let [part (get ship-json 0)]
    {:state (get ship-json 1)
     :components {:dice1HPmissile (part "dice1HPmissile")
                  :dice2HPmissile (part "dice2HPmissile")
                  :dice1HP (part "dice1HP") :dice2HP (part "dice2HP")
                  :dice4HP (part "dice4HP") :computer (part "computer")
                  :hull (part "hull") :shield (part "shield")}
     :hits (empty-hits-vector (part "hull")) :alive 1 :init i}))

(defn hull-hit-combinations
  "Takes the amount of hull as an integer and a vector containing hit combina-
  tions and returns the number of combinations where the ship has not yet
  received a fatal amount of hits."
  [hull hits]
  (let [indexes (range 1 (+ 2 hull))]
    (apply + (map (fn [i] (get hits i)) indexes))))

(defn alive-odds
  "Takes a map presentation of a ship and returns the odds the ship is still
  alive as a ratio."
  [ship]
  (let [hits (:hits ship)
        all-combinations (get hits 0)
        hull (component ship :hull)]
    (if (has-no-hits? ship) 1
      (/ (hull-hit-combinations hull hits) all-combinations))))

(defn targets-for
  "Takes a string presentation of status and a list containing map presentations 
  of ships, loops through the list and returns those with unmatching states, until
  one opponent with 100% odds for being alive is found."
  [state all-ships]
  (loop [targets [] i 0 has-enough false]
    (if (or has-enough (= i (count all-ships))) targets
      (let [ship (all-ships i)
            opponent (is-opponent-for? state ship)]
        (recur (if opponent (conj targets ship) targets)
               (inc i) (if (and opponent (= 1 (alive-odds ship))) true false))))))

(defn hit-once-odds
  "Takes two map presentations of ships and returns the odds for the first one
  of them to hit the second with one dice. Includes shield and computer improve-
  ments. Considers 6 always a hit and 1 always a miss as set by game rules. Takes
  previous hits into consideration by multiplying the oss with odds to be alive."
  [ship-a ship-d]
  (let [prev-odds (alive-odds ship-d)]
  (let [numerator (+ 1 (component ship-a :computer) (component ship-d :shield))]
    (if (> numerator 1)
      (if (< numerator 6) (* prev-odds (/ numerator 6)) (* prev-odds(/ 5 6)))
      (* prev-odds (/ 1 6))))))

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
  (let [damage (some #(if (< 0 %) %) (keys freq))]
    (if (<= i damage) (if (= 0 (get hits i)) (if (= 1 i) (freq 0) 0)
                        (* (get hits i) (freq 0)))
      (if (= 0 (get hits 1)) (if (= (inc damage) i) (freq damage) 0)
        (+ (* (freq damage) (get hits (- i damage))) (* (get hits i) (freq 0)))))))

(defn add-combinations
  "Takes previous hits as a vector and a map containing hit frequencies for a 
  single weapon hit. Returns a new hits vector containing updated hit combinations."
  [hits freq]
  (let [damage (some #(if (< 0 %) %) (keys freq))
        hull (count hits)]
    (loop [acc [(* (apply + (vals freq)) (get hits 0))] i 1]
      (if (>= i hull) acc
        (recur (conj acc (combination hits i freq)) (inc i))))))

(defn all-weapon-combinations
  "Takes previous hits as vector, the number of weapons and damage value for those
  weapons, as well as odds for hitting the target and returns a vector containing
  combinations of all hits produced by those weapons, added on top of the combi-
  nations of any previous hits."
  [hits weapons damage odds]
  (loop [acc hits i weapons]
    (if (zero? i)
      acc
      (recur (add-combinations acc (dice-hit-freq damage odds)) (dec i)))))

(defn attack-with-cannons
  "Takes two map presentations of ships, the attacking ship and it's target.
  Returns the target with updated hit counter according to attacker cannons"
  [ship-a ship-d]
  (let [hp1 (component ship-a :dice1HP)
        hp2 (component ship-a :dice2HP)
        hp4 (component ship-a :dice4HP)
        odds (hit-once-odds ship-a ship-d)
        hits (:hits ship-d)
        hp1-hits (all-weapon-combinations hits hp1 1 odds)
        hp2-hits (all-weapon-combinations hp1-hits hp2 2 odds)
        hp4-hits (all-weapon-combinations hp2-hits hp4 4 odds)
        ship-d-with-hits (assoc ship-d :hits hp4-hits)]
    (assoc ship-d-with-hits :alive (alive-odds ship-d-with-hits))))

(defn target-and-attack-cannons
  "Takes a map presentation of attacking ship and a vector containing all ships,
  chooses targets to the attacker, attacks them and returns a new vector containing
  updated hit vectors for affected ships."
  [ship-a ships]
  (let [targets (targets-for (:state ship-a) ships)
        end-i (count targets)]
    (loop [new-ships ships i 0]
      (if (= i end-i) new-ships
        (recur (assoc new-ships (:init (targets i))
                      (attack-with-cannons ship-a (targets i)))
               (inc i))))))

(defn cannons-round
  "Takes a map presentation of ships and adds attacks from cannons to to enemy
  hit vectors. Returns a new map presentation of ships with updated hit vectors."
  ([ships]
    (loop [new-ships ships i 0]
      (if (= i (count ships)) new-ships 
        (recur (target-and-attack-cannons (ships i) new-ships) (inc i)))))
  ([ships rounds]
   (loop [new-ships ships i rounds]
     (if (zero? i) new-ships
       (recur (cannons-round new-ships) (dec i))))))

(defn attack-with-missiles
  "Takes two map presentations of ships, the attacking ship and it's target.
  Returns the target with updated hit counter according to attacker missiles,
  as well as new alive odds for calculating future hit odds."
  [ship-a ship-d]
  (let [hp1 (component ship-a :dice1HPmissile)
        hp2 (component ship-a :dice2HPmissile)
        odds (hit-once-odds ship-a ship-d)
        hits (:hits ship-d)
        hp1-hits (all-weapon-combinations hits hp1 1 odds)
        hp2-hits (all-weapon-combinations hp1-hits hp2 2 odds)
        ship-d-with-hits (assoc ship-d :hits hp2-hits)]
    (assoc ship-d-with-hits :alive (alive-odds ship-d-with-hits))))

(defn target-and-attack-missiles
  "Takes a map presentation of attacking ship and a vector containing all ships,
  chooses targets to the attacker, attacks them and returns a new vector containing
  updated hit vectors for affected ships."
  [ship-a ships]
  (let [targets (targets-for (:state ship-a) ships)
        end-i (count targets)]
    (loop [new-ships ships i 0]
      (if (= i end-i) new-ships
        (recur (assoc new-ships (:init (targets i))
                      (attack-with-missiles ship-a (targets i)))
               (inc i))))))

(defn has-missiles?
  "Takes a map presentation of a ship and checks if the ship has any missile
  weaponry installed. Returns true if it has."
  [ship]
  (or
    (< 0 (component ship :dice1HPmissile))
    (< 0 (component ship :dice2HPmissile))))

(defn missiles-round
  "Takes a map presentation of ships, checks each ship for missiles and if they
  have any, adds those attacks to enemy hit vectors. Returns a new map presentation
  of ships with updated hit vectors."
  [ships]
  (loop [new-ships ships i 0]
    (if (= i (count ships)) new-ships 
      (recur (if (has-missiles? (ships i))
               (target-and-attack-missiles (ships i) new-ships)
              new-ships) (inc i)))))
