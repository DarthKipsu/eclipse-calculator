(ns eclipse.core.core-test
  (:require [clojure.test :refer :all]
            [eclipse.core.core :refer :all])
  (:use midje.sweet))

(def json-2-int
  [[{"type" "interceptor",
     "one" nil, "two" "ionCannon", "three" "nuclearSource", "four" "nuclearDrive",
     "dice1HPmissile" 0, "dice2HPmissile" 0,
     "dice1HP" 1, "dice2HP" 0, "dice4HP" 0,
     "computer" 0, "shield" 0, "hull" 0,
     "initiative" 3,
     "energy" 1, "speed" 1}, "defender"],
   [{"type" "interceptor",
     "one" nil, "two" "ionCannon", "three" "nuclearSource", "four" "nuclearDrive",
     "dice1HPmissile" 0, "dice2HPmissile" 0,
     "dice1HP" 1, "dice2HP" 0, "dice4HP" 0,
     "computer" 0, "shield" 0, "hull" 0,
     "initiative" 3,
     "energy" 1, "speed" 1}, "attacker"]])

(def reform-2-int
  [{:state "defender",
   :components {:dice1HPmissile 0, :dice2HPmissile 0,
                :dice1HP 1, :dice2HP 0, :dice4HP 0,
                :computer 0, :shield 0, :hull 0}
   :hits [1 0], :alive 1},
   {:state "attacker",
   :components {:dice1HPmissile 0, :dice2HPmissile 0,
                :dice1HP 1, :dice2HP 0, :dice4HP 0,
                :computer 0, :shield 0, :hull 0}
   :hits [1 0], :alive 1}])



(facts "array reforming"
  (fact "transforms the vector received from ui to a more usable form"
        (reform-ships json-2-int) =future=> reform-2-int))
