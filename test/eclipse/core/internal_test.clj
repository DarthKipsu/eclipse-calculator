(ns eclipse.core.internal-test
  (:require [clojure.test :refer :all]
            [eclipse.core.internal :refer :all]
            [eclipse.core.internal-test-data :refer :all])
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

(facts "get-hit-probabilities"
  (fact "1/6 odds when no modifiers"
        (hit-once-odds att-int def-int) => 1/9)
  (fact "only 5/6 odds even with insane computer bonus"
        (hit-once-odds att-dre def-int) => 5/6)
  (fact "1/6 odds even against insane shield bonuses"
        (hit-once-odds def-int att-dre) => 1/6)
  (fact "multiplies the dice odds with alive odds to account for previous hits"
        (hit-once-odds att-dre def-cru) => 25/54))

(facts "binomial probabilities"
  (fact "Binomial coefficient is calculated correctly"
        (bin-coef 1 1) => 1
        (bin-coef 5 3) => 10
        (bin-coef 15, 5) => 3003)
  (fact "binomial distribution is calculated correctly"
        (bin-dist 1 1 1/6) => (roughly 1/6)
        (bin-dist 2 1 1/6) => (roughly 5/18)
        (bin-dist 5 3 2/6) => (roughly 40/243)))

(facts "hit odds"
  (fact "dice hit frequencies return correct values"
        (dice-hit-freq 1 1/6) => {1 1 0 5}
        (dice-hit-freq 2 1/6) => {2 1 0 5}
        (dice-hit-freq 4 1/6) => {4 1 0 5}
        (dice-hit-freq 1 2/6) => {1 1 0 2}
        (dice-hit-freq 1 3/6) => {1 1 0 1}
        (dice-hit-freq 1 4/6) => {1 2 0 1}
        (dice-hit-freq 1 5/6) => {1 5 0 1}
        (dice-hit-freq 1 25/54) => {1 25 0 29})
  (fact "returns new combinations for a single hull value"
        (combination [6 5 0] 1 {1 1 0 5}) => 25
        (combination [6 5 0] 2 {1 1 0 5}) => 5
        (combination [36 20 4 10 2] 1 {2 1 0 5}) => 100
        (combination [36 20 4 10 2] 2 {2 1 0 5}) => 20
        (combination [36 20 4 10 2] 3 {2 1 0 5}) => 70
        (combination [36 20 4 10 2] 4 {2 1 0 5}) => 14)
  (fact "adds a single weapon odds to all hit combinations"
        (add-combinations [1 0] {1 1 0 5}) => [6 5]
        (add-combinations [1 0] {2 1 0 2}) => [3 2]
        (add-combinations [1 0 0] {2 1 0 1}) => [2 1 0]
        (add-combinations [1 0 0 0] {2 1 0 5}) => [6 5 0 1]
        (add-combinations [3 2 1 0] {1 1 0 2}) => [9 4 4 1]
        (add-combinations [1 0 0 0 0] {2 1 0 5}) => [6 5 0 1 0]
        (add-combinations [3 2 0 1 0 0] {2 1 0 2}) => [9 4 0 4 0 1]
        (add-combinations [6 5 1 0 0] {2 1 0 2}) => [18 10 2 5 1]
        (add-combinations [1 0 0 0 0 0] {4 1 0 2}) => [3 2 0 0 0 1]
        (add-combinations [18 10 2 5 1 0] {2 1 0 5}) => [108 50 10 35 7 5])
  (fact "adds all hits for a type of weapon"
        (all-weapon-combinations [1 0] 1 1 1/6) => [6 5]
        (all-weapon-combinations [1 0] 2 1 1/6) => [36 25]
        (all-weapon-combinations [1 0 0] 1 1 1/6) => [6 5 1]
        (all-weapon-combinations [1 0 0 0] 2 2 2/6) => [9 4 0 4]
        (all-weapon-combinations [6 5 1 0 0] 2 1 2/6) => [54 20 24 9 1])
  (fact "returns correct alive combinations for all hull levels"
        (hull-hit-combinations 0 [6 5]) => 5
        (hull-hit-combinations 1 [6 5 1]) => 6
        (hull-hit-combinations 1 [36 20 4 10 2]) => 24
        (hull-hit-combinations 3 [36 20 4 10 2]) => 36
        (hull-hit-combinations 4 [1296 300 360 270 252 72]) => 1254))

(facts "alive odds"
  (fact "odds to be still alive are returned correctly"
        (alive-odds def-int) => 1
        (alive-odds def-cru) => 25/36
        (alive-odds att-int) => 2/3
        (alive-odds alive01) => 25/54
        (alive-odds alive02) => 85/108
        (alive-odds alive03) => 25/27
        (alive-odds alive04) => 107/108
        (alive-odds alive05) => 1
        (alive-odds alive06) => 212/216
        (alive-odds alive07) => 660/1296)
  (fact "formatted alive percentages are returned for a group of ships"
        (alive-odds-formatted [missile-03 cannon-03-hit]) => ["100,0" "66,7"]
        (alive-odds-formatted [alive08 alive09 alive10]) => ["50,0" "3,7" "99,1"]
        (alive-odds-formatted [alive11 alive12 alive13]) => ["92,6" "46,9" "55,7"]
        (alive-odds-formatted [alive14]) => ["100,0"])
  (fact "calculates odds for none of the given ships to be alive correctly"
        (none-alive-odds [alive08]) => 0.5
        (none-alive-odds [alive08 alive12 alive13]) => (roughly 0.117 1E-3) 
        (none-alive-odds [alive09 alive12]) => (roughly 0.511 1E-3) 
        (none-alive-odds [alive10 alive11]) => (roughly 0.0006 1E-3)
        (none-alive-odds [alive14]) => 0.0))

(facts "winner odds"
  (fact "returns corrects odds for player to be alive while no opponents are left"
        (opponent-destroyed 0.100 0.855) => (roughly 0.77 1E-3)
        (opponent-destroyed 0.666 0.999) => (roughly 0.333 1E-3)
        (opponent-destroyed 0.535 0.632) => (roughly 0.293 1E-3)
        (opponent-destroyed 0.632 0.535) => (roughly 0.197 1E-3))
  (fact "returns win odds for defender side"
        (win-odds-defender [alive08] [alive09]) => (roughly 0.963 1E-3)
        (win-odds-defender [alive09 alive10] [alive11 alive12]) =>
          (roughly 0.82 1E-3)
        (win-odds-defender [alive13 alive12 alive08] [alive10 alive11]) =>
          (roughly 0.005 1E-3)
        (win-odds-defender [alive14] [alive14]) => 0.0
        (win-odds-defender [alive14] [alive15]) => 1.0)
  (fact "returns win odds for attacker side"
        (win-odds-attacker 0.5 []) => 0.5
        (win-odds-attacker 0.126 []) => 0.874
        (win-odds-attacker 0.75 []) => 0.25
        (win-odds-attacker 0.0 [alive14]) => 0.0
        (win-odds-attacker 0.0 [alive15]) => 1.0))
        

(facts "vector reforming helpers"
  (fact "creates hit vectors of correct size"
        (empty-hits-vector 0) => [1 0]
        (empty-hits-vector 1) => [1 0 0]
        (empty-hits-vector 2) => [1 0 0 0]
        (empty-hits-vector 3) => [1 0 0 0 0])
  (fact "turns a json originated vector to an usable form"
        (reform-single-ship 0 json-01) => reform-01
        (reform-single-ship 1 json-02) => reform-02))
