(ns eclipse.core.internal-test-data)

(def att-int
  {:state "attacker",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 1,
                :dice1HP 1,
                :dice2HP 0,
                :dice4HP 0,
                :computer 0,
                :shield 0,
                :hull 1}
   :hits [9 5 1]
   :alive 1})

(def att-dre
  {:state "attacker",
   :components {:dice1HPmissile 1,
                :dice2HPmissile 0,
                :dice1HP 0,
                :dice2HP 2,
                :dice4HP 2,
                :computer 5,
                :shield -5,
                :hull 5}
   :hits [1 0 0 0 0 0 0]
   :alive 1})

(def def-int
  {:state "defender",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 0,
                :dice1HP 0,
                :dice2HP 1,
                :dice4HP 0,
                :computer 0,
                :shield 0,
                :hull 0}
   :hits [1 0]
   :alive 1})

(def def-cru
  {:state "defender",
   :components {:dice1HPmissile 1,
                :dice2HPmissile 2,
                :dice1HP 1,
                :dice2HP 1,
                :dice4HP 1,
                :computer 2,
                :shield -2,
                :hull 1}
   :hits [36 25 0]
   :alive 1})

(def json-01
  [{"type" "interceptor",
    "one" nil, "two" "ionCannon", "three" "nuclearSource", "four" "nuclearDrive",
    "dice1HPmissile" 0, "dice2HPmissile" 0,
    "dice1HP" 1, "dice2HP" 0, "dice4HP" 0,
    "computer" 0, "shield" 0, "hull" 0,
    "initiative" 3, "energy" 1, "speed" 1}, "defender"])

(def json-02
  [{"type" "dreadnought",
    "one" "plasmaCannon", "two" "shardHull", "three" "improvedHull",
    "four" "plasmaCannon", "five" "tachyonSource", "six" "plasmaCannon",
    "seven" "gluonComputer", "eight" "nuclearDrive",
    "dice1HPmissile" 0, "dice2HPmissile" 0,
    "dice1HP" 0, "dice2HP" 3, "dice4HP" 0,
    "computer" 3, "shield" 0, "hull" 5,
    "initiative" 3, "energy" 0, "speed" 1}, "defender"])

(def reform-01
  {:state "defender",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 0,
                :dice1HP 1,
                :dice2HP 0,
                :dice4HP 0,
                :computer 0,
                :shield 0,
                :hull 0}
   :hits [1 0],
   :alive 1})

(def reform-02
  {:state "defender",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 0,
                :dice1HP 0,
                :dice2HP 3,
                :dice4HP 0,
                :computer 3,
                :shield 0,
                :hull 5}
   :hits [1 0 0 0 0 0 0],
   :alive 1})

(def alive01 {:components {:hull 0} :hits [216 100]})
(def alive02 {:components {:hull 1} :hits [216 100 70]})
(def alive03 {:components {:hull 2} :hits [216 100 70 30]})
(def alive04 {:components {:hull 3} :hits [216 100 70 30 14]})
(def alive05 {:components {:hull 4} :hits [216 100 70 30 14 2]})
(def alive06 {:components {:hull 2} :hits [216 80 96 36]})
(def alive07 {:components {:hull 1} :hits [1296 300 360]})
