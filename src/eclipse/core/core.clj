(ns eclipse.core.core
  (:use eclipse.core.internal))

(defn reform-ships
  "Takes a vector containing ships involved in the battle, reforms them to be
  usable for probability calculations and returns a new array containing the 
  reformed ships. Unfinished."
  [ships]
  (mapv reform-single-ship ships))
