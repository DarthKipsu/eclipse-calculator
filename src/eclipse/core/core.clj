(ns eclipse.core.core
  (:use eclipse.core.internal.attacks
        eclipse.core.internal.odds
        eclipse.core.internal.reforms))

(defn- mapv-indexed [f v]
  (into [] (map-indexed f v)))

(defn reform-ships
  "Takes a vector containing ships involved in the battle, reforms them to be
  usable for probability calculations and returns a new array containing the 
  reformed ships."
  [ships]
  (mapv-indexed reform-single-ship ships))

(defn win-probabilities
  "Takes a vector containig map presentations of ships and returns a map contai-
  ning win probabilities for both sides as well as vector containing probabilities
  for each ship to be alive after the match."
  [ships]
  (let [ships-missiles (missiles-round ships)
        ships-cannons (cannons-round ships-missiles 3)
        defenders (targets-for "attacker" ships-cannons)
        attackers (targets-for "defender" ships-cannons)
        def-win-odds (win-odds-defender defenders attackers)
        att-win-odds (win-odds-attacker def-win-odds defenders)]
    {"defender" (format "%.1f" (* 100 def-win-odds))
     "attacker" (format "%.1f" (* 100 att-win-odds))
     "alive-odds" (alive-odds-formatted ships-cannons)}))
