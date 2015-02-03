(ns eclipse.core.internal.attacks
  (:require [clojure.math.numeric-tower :as math]
            [eclipse.core.internal.my-clj-function-implementations :refer :all])
  (:use [eclipse.core.internal.odds]))

(defn- is-opponent-for? [state, target]
  (not= state (:state target)))

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
