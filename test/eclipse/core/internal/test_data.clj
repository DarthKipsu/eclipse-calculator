(ns eclipse.core.internal.test-data)

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
   :alive 2/3})

(def att-int-2
  {:state "attacker",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 1,
                :dice1HP 0,
                :dice2HP 1,
                :dice4HP 0,
                :computer 0,
                :shield 0,
                :hull 1}
   :hits [9 5 1]
   :alive 2/3})

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
   :alive 1
   :init 1})

(def def-int-with-hit
  {:state "defender",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 0,
                :dice1HP 0,
                :dice2HP 1,
                :dice4HP 0,
                :computer 0,
                :shield 0,
                :hull 0}
   :hits [9 8]
   :alive 8/9
   :init 1})

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
   :alive 25/36
   :init 0})

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

(def json-int
  [{"type" "interceptor",
    "one":null, "two" "ionCannon", "three" "nuclearSource", "four" "nuclearDrive",
    "dice1HPmissile" 0, "dice2HPmissile" 0, "dice1HP" 1, "dice2HP" 0, "dice4HP" 0,
    "computer" 0, "shield" 0, "hull" 0, "initiative" 3, "energy" 1, "speed" 1},
   "defender"])

(def json-int2
  [{"type" "interceptor",
    "one":null, "two" "ionCannon", "three" "nuclearSource", "four" "nuclearDrive",
    "dice1HPmissile" 0, "dice2HPmissile" 0, "dice1HP" 1, "dice2HP" 0, "dice4HP" 0,
    "computer" 0, "shield" 0, "hull" 0, "initiative" 3, "energy" 1, "speed" 1},
   "attacker"])

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
   :alive 1
   :init 0})

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
   :alive 1
   :init 1})

(def ref-int
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
   :alive 1
   :init 0})

(def ref-int-h1
  {:state "defender",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 0,
                :dice1HP 1,
                :dice2HP 0,
                :dice4HP 0,
                :computer 0,
                :shield 0,
                :hull 0}
   :hits [36 31],
   :alive 31/36
   :init 0})

(def ref-int2
  {:state "attacker",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 0,
                :dice1HP 1,
                :dice2HP 0,
                :dice4HP 0,
                :computer 0,
                :shield 0,
                :hull 0}
   :hits [1 0],
   :alive 1
   :init 1})

(def ref-int2-h1
  {:state "attacker",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 0,
                :dice1HP 1,
                :dice2HP 0,
                :dice4HP 0,
                :computer 0,
                :shield 0,
                :hull 0}
   :hits [6 5],
   :alive 5/6
   :init 1})

(def alive01 {:components {:hull 0} :hits [216 100]})
(def alive02 {:components {:hull 1} :hits [216 100 70]})
(def alive03 {:components {:hull 2} :hits [216 100 70 30]})
(def alive04 {:components {:hull 3} :hits [216 100 70 30 14]})
(def alive05 {:components {:hull 4} :hits [216 100 70 30 14 2]})
(def alive06 {:components {:hull 2} :hits [216 80 96 36]})
(def alive07 {:components {:hull 1} :hits [1296 300 360]})

(def alive08 {:alive 1/2})
(def alive09 {:alive 1/27})
(def alive10 {:alive 107/108})
(def alive11 {:alive 25/27})
(def alive12 {:alive 120/256})
(def alive13 {:alive 1218/2187})
(def alive14 {:alive 1})
(def alive15 {:alive 0})

(def missile-01
  {:state "defender",
   :components {:dice1HPmissile 1,
                :dice2HPmissile 1,
                :dice1HP 1,
                :dice2HP 1,
                :dice4HP 0,
                :computer 2,
                :shield 0,
                :hull 1}
   :hits [1 0 0]
   :alive 1
   :init 0})

(def missile-01-hit-2
  {:state "defender",
   :components {:dice1HPmissile 1,
                :dice2HPmissile 1,
                :dice1HP 1,
                :dice2HP 1,
                :dice4HP 0,
                :computer 2,
                :shield 0,
                :hull 1}
   :hits [9 1 4]
   :alive 5/9
   :init 0})

(def missile-01-hit-3
  {:state "defender",
   :components {:dice1HPmissile 1,
                :dice2HPmissile 1,
                :dice1HP 1,
                :dice2HP 1,
                :dice4HP 0,
                :computer 2,
                :shield 0,
                :hull 1}
   :hits [729 121 352]
   :alive 473/729
   :init 0})

(def missile-02
  {:state "attacker",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 0,
                :dice1HP 0,
                :dice2HP 2,
                :dice4HP 0,
                :computer 1,
                :shield 0,
                :hull 2}
   :hits [1 0 0 0]
   :alive 1
   :init 1})

(def missile-02-hit-2
  {:state "attacker",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 0,
                :dice1HP 0,
                :dice2HP 2,
                :dice4HP 0,
                :computer 1,
                :shield 0,
                :hull 2}
   :hits [4 1 1 1]
   :alive 3/4
   :init 1})

(def missile-02-hit-4
  {:state "attacker",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 0,
                :dice1HP 0,
                :dice2HP 2,
                :dice4HP 0,
                :computer 1,
                :shield 0,
                :hull 2}
   :hits [256 25 40 55]
   :alive 120/256
   :init 1})

(def missile-03
  {:state "attacker",
   :components {:dice1HPmissile 2,
                :dice2HPmissile 0,
                :dice1HP 2,
                :dice2HP 0,
                :dice4HP 0,
                :computer 3,
                :shield -1,
                :hull 2}
   :hits [1 0 0 0]
   :alive 1
   :init 2})

(def missile-03-hit-2
  {:state "attacker",
   :components {:dice1HPmissile 2,
                :dice2HPmissile 0,
                :dice1HP 2,
                :dice2HP 0,
                :dice4HP 0,
                :computer 3,
                :shield -1,
                :hull 2}
   :hits [9 4 2 2]
   :alive 8/9
   :init 2})

(def cannon-01
  {:state "attacker",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 1,
                :dice1HP 1,
                :dice2HP 1,
                :dice4HP 1,
                :computer 1,
                :shield 0,
                :hull 2}
   :hits [1 0 0 0]
   :alive 1
   :init 0})

(def cannon-01-hit
  {:state "attacker",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 1,
                :dice1HP 1,
                :dice2HP 1,
                :dice4HP 1,
                :computer 1,
                :shield 0,
                :hull 2}
   :hits [81 16 0 16]
   :alive 32/81
   :init 0})

(def cannon-01-hit-2
  {:state "attacker",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 1,
                :dice1HP 1,
                :dice2HP 1,
                :dice4HP 1,
                :computer 1,
                :shield 0,
                :hull 2}
   :hits [6561 2401 0 1372]
   :alive 3773/6561
   :init 0})

(def cannon-02
  {:state "attacker",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 0,
                :dice1HP 0,
                :dice2HP 2,
                :dice4HP 0,
                :computer 1,
                :shield 0,
                :hull 1}
   :hits [1 0 0]
   :alive 1
   :init 2})

(def cannon-03
  {:state "defender",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 0,
                :dice1HP 0,
                :dice2HP 2,
                :dice4HP 2,
                :computer 1,
                :shield 0,
                :hull 3}
   :hits [1 0 0 0 0]
   :alive 1
   :init 1})

(def cannon-03-hit
  {:state "defender",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 0,
                :dice1HP 0,
                :dice2HP 2,
                :dice4HP 2,
                :computer 1,
                :shield 0,
                :hull 3}
   :hits [27 8 4 4 2]
   :alive 2/3
   :init 1})

(def cannon-03-hit-2
  {:state "defender",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 0,
                :dice1HP 0,
                :dice2HP 2,
                :dice4HP 2,
                :computer 1,
                :shield 0,
                :hull 3}
   :hits [2187 392 196 420 210]
   :alive 406/729
   :init 1})

(def cannon-04
  {:state "defender",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 1,
                :dice1HP 0,
                :dice2HP 1,
                :dice4HP 0,
                :computer 1,
                :shield 0,
                :hull 1}
   :hits [1 0 0]
   :alive 1
   :init 0})

(def cannon-04-hit-3
  {:state "defender",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 1,
                :dice1HP 0,
                :dice2HP 1,
                :dice4HP 0,
                :computer 1,
                :shield 0,
                :hull 1}
   :hits [1734183678 558989220 701338476]
   :alive 210054616/289030613
   :init 0})

(def cannon-04-hit-3-missiles
  {:state "defender",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 1,
                :dice1HP 0,
                :dice2HP 1,
                :dice4HP 0,
                :computer 1,
                :shield 0,
                :hull 1}
   :hits [1152549034 461567602 457703714]
   :alive 459635658/576274517
   :init 0})

(def cannon-05
  {:state "attacker",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 0,
                :dice1HP 2,
                :dice2HP 0,
                :dice4HP 0,
                :computer 1,
                :shield 0,
                :hull 1}
   :hits [1 0 0]
   :alive 1
   :init 1})

(def cannon-05-hit-3
  {:state "attacker",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 0,
                :dice1HP 2,
                :dice2HP 0,
                :dice4HP 0,
                :computer 1,
                :shield 0,
                :hull 1}
   :hits [9514694705 4276848950 0]
   :alive 855369790/1902938941
   :init 1})

(def cannon-05-hit-3-and-missiles
  {:state "attacker",
   :components {:dice1HPmissile 0,
                :dice2HPmissile 0,
                :dice1HP 2,
                :dice2HP 0,
                :dice4HP 0,
                :computer 1,
                :shield 0,
                :hull 1}
   :hits [1077526366 405991393 0]
   :alive 405991393/1077526366
   :init 1})
