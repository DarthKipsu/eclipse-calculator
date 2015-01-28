(ns eclipse.core.core
  (:use eclipse.core.internal))

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
  (let [ships-after-missiles (missiles-round ships)]
    (cannons-round ships-after-missiles 3)))
