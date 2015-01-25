(ns eclipse.core.core
  (:use eclipse.core.internal))

(defn- mapv-indexed [f v]
  (into [] (map-indexed f v)))

(defn reform-ships
  "Takes a vector containing ships involved in the battle, reforms them to be
  usable for probability calculations and returns a new array containing the 
  reformed ships. Unfinished."
  [ships]
  (mapv-indexed reform-single-ship ships))
