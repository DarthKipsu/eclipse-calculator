(ns eclipse.core.internal.attacks-test
  (:require [clojure.test :refer :all]
            [eclipse.core.internal.attacks :refer :all]
            [eclipse.core.internal.test-data :refer :all])
  (:use midje.sweet))

(facts "filter and return targets"
  (fact "the correct amount of target ships can be found"
        (count (targets-for "attacker" [def-int att-int])) => 1
        (count (targets-for "defender" [def-int def-int])) => 0
        (count (targets-for "defender" [def-int att-int att-int])) => 2 
        (count (targets-for "defender" [def-int att-dre att-dre])) => 1)
  (fact "the state of the target is correct"
        (:state (get (targets-for "attacker" [def-int att-int]) 0)) => "defender"
        (:state (get (targets-for "defender" [def-int att-int]) 0)) => "attacker")
  (fact "the targets are returned inside a vector"
        (targets-for "defender" [def-int att-int att-dre]) => [att-int att-dre]
        (targets-for "attacker" [att-int def-int att-dre]) => [def-int]))

(facts "missiles"
  (fact "has-missiles? returns true when ship has at least one missile"
        (has-missiles? att-int)
        (has-missiles? att-dre)
        (has-missiles? def-cru))
  (fact "defender interceptor does not have missiles"
        (has-missiles? def-int) => falsey)
  (fact "adds attacker missiles to defenders hits vector"
        (:hits (attack-with-missiles att-int def-int)) => [9 8]
        (:hits (attack-with-missiles def-cru def-int)) => [373248 103823]
        (:hits (attack-with-missiles def-cru att-int)) => [11337408 2858935 1432912])
  (fact "targets and attacks correct enemies with missiles"
        (target-and-attack-missiles att-int [att-int def-int att-dre]) => 
          [att-int def-int-with-hit att-dre]
        (target-and-attack-missiles att-int [att-int def-int def-int att-dre]) => 
          [att-int def-int-with-hit def-int att-dre]
        (target-and-attack-missiles missile-01 [missile-01 missile-02]) => 
          [missile-01 missile-02-hit-2]
        (target-and-attack-missiles 
          missile-01 [missile-01 missile-02-hit-2 missile-03]) => 
          [missile-01 missile-02-hit-4 missile-03-hit-2])
  (fact "plays a missile round for all ships"
        (missiles-round [missile-01 missile-02 missile-03]) =>
          [missile-01-hit-2 missile-02-hit-2 missile-03]
        (missiles-round [missile-01 missile-02-hit-2 missile-03]) =>
          [missile-01-hit-2 missile-02-hit-4 missile-03-hit-2]))

(facts "cannons"
  (fact "adds attacker cannons to defenders hits vector"
        (:hits (attack-with-cannons att-int def-int)) => [9 8]
        (:hits (attack-with-cannons def-cru def-int)) => [373248 103823]
        (:hits (attack-with-cannons def-cru att-int)) => [11337408 2858935 1432912]
        (:hits (attack-with-cannons def-cru att-dre)) =>
          [10077696 6967871 912025 912025 119375 912025 119375])
  (fact "targets and attacks correct enemies with missiles"
        (target-and-attack-cannons att-int-2 [att-int-2 def-int att-dre]) => 
          [att-int-2 def-int-with-hit att-dre]
        (target-and-attack-cannons cannon-01 [cannon-01 cannon-03 cannon-02]) => 
          [cannon-01 cannon-03-hit cannon-02]
        (target-and-attack-cannons cannon-03 [cannon-01 cannon-03 cannon-02]) => 
          [cannon-01-hit cannon-03 cannon-02])
  (fact "plays a cannon round for all ships"
        (cannons-round [cannon-01 cannon-03]) =>
          [cannon-01-hit cannon-03-hit]
        (cannons-round [cannon-01 cannon-03 cannon-02]) =>
          [cannon-01-hit cannon-03-hit-2 cannon-02])
  (fact "plays three rounds of cannons for all ships"
        (cannons-round [cannon-04 cannon-05] 3) => 
          [cannon-04-hit-3 cannon-05-hit-3])
  (fact "plays three rounds after a missile round"
        (cannons-round (missiles-round [cannon-04 cannon-05]) 3) =>
          [cannon-04-hit-3-missiles cannon-05-hit-3-and-missiles]))
