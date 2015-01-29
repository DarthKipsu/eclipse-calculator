(ns eclipse.core.my-clj-function-implementations)

(defn my-get-in
  "takes a map and a vector containing a path inside that map as a parameter and
  returns the value nested inside the sequence of keys in the given vector."
  [a-map key-seq]
  (loop [acc a-map i 0]
    (if (= i (count key-seq))
      acc
      (recur ((key-seq i) acc) (+ i 1)))))

(defn my-repeat
  "takes a value of any type and a multiplier and returns a lazy sequence with
  the value repeated by the multiplier."
  ([n value] (my-repeat n value '()))
  ([n value a-seq]
   (if (zero? n)
     a-seq
     (recur (- n 1) value (conj a-seq value)))))
