(ns eclipse.core.core)

(defn reform-ships
  "Takes a vector containing ships involved in the battle, reforms them to be
  usable for probability calculations and returns a new array containing the 
  reformed ships. Unfinished."
  [ships]
  (map (fn [ship] ship) ships))
