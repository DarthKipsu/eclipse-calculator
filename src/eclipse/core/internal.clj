(ns eclipse.core.internal)

(defn- is-opponent-for? [state]
  (fn [target] (not= state (:state target))))

(defn targets-for
  "Takes a string presentation of status and a list containing map presentations 
  of ships, filters through the list and returns those with unmatching states.
  TODO: filterv"
  [state all-ships]
  (filterv (is-opponent-for? state) all-ships))

(defn has-missiles?
  "Takes a map presentation of a ship and checks if the ship has any missile
  weaponry installed. Returns true if it has."
  [ship]
  (or
    (< 0 (get-in ship [:components :dice1HPmissile]))
    (< 0 (get-in ship [:components :dice2HPmissile]))))
