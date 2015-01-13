(ns eclipse.core.internal)

(defn- is-opponent-for? [state]
  (fn [target] (not= state (:state target))))

(defn- component [ship component]
  (get-in ship [:components component]))

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
    (< 0 (component ship :dice1HPmissile))
    (< 0 (component ship :dice2HPmissile))))

(defn hit-once-odds
  "Takes two map presentations of ships and returns the odds for the first one
  of them to hit the second with one dice."
  [ship-a ship-d]
  (let [numerator (+ 1 (component ship-a :computer) (component ship-d :shield))]
    (if (> numerator 1)
      (if (< numerator 6) (/ numerator 6) (/ 5 6))
      (/ 1 6))))
