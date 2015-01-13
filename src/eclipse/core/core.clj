(ns eclipse.core.core)

(defn are-opponents
  "Takes two map presentations of ships and checks their states. Returns true 
  if the states are not a match."
  [ship-a ship-b]
  (not= (:state ship-a) (:state ship-b)))

(defn targets-for
  "Takes a map presentation of a ship and a list containing map presentations 
  of ships, filters through the list and returns those with unmatching states.
  TODO: filterv"
  [ship all-ships]
  (filterv (fn [target] (are-opponents ship target)) all-ships))
