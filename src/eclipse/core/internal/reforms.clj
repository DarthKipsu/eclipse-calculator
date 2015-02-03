(ns eclipse.core.internal.reforms
  (:require [eclipse.core.internal.my-clj-function-implementations :refer :all]))

(defn empty-hits-vector 
  "takes a hull count and returns an empty hits vector with enough slots for 
  each hull"
  [hull]
  (apply conj [1] (my-repeat (inc hull) 0)))

(defn reform-single-ship
  "Takes a vector containing a ship presentation returned by the ui and returns
  a new ship map structure created based on the input."
  [i ship-json]
  (let [part (ship-json 0)]
    {:state (ship-json 1)
     :components {:dice1HPmissile (part "dice1HPmissile")
                  :dice2HPmissile (part "dice2HPmissile")
                  :dice1HP (part "dice1HP") :dice2HP (part "dice2HP")
                  :dice4HP (part "dice4HP") :computer (part "computer")
                  :hull (part "hull") :shield (part "shield")}
     :hits (empty-hits-vector (part "hull")) :alive 1 :init i}))
