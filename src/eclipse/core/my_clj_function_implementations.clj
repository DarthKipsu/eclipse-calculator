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

(defn apply-all 
  "a helper function for my-map that takes a function and a sequence, goes through
  the sequence and applies the function to each value and then returns the results
  of the function in a sequence."
  ([func a-seq] (apply-all func a-seq '()))
  ([func a-seq acc]
   (if (empty? a-seq)
     (reverse acc)
     (recur func (rest a-seq) (cons (func (first a-seq)) acc)))))

(defn my-map
  "takes a function and one or more sequences and returns a sequence containing
  the results of applying the function to the first set of parameters, then the 
  second and so on until reaching the end of one or more of the sequences."
  ([func a-seq] (if (empty? a-seq) '() (apply-all func a-seq)))
  ([func a-seq & more]
   (if (or (empty? a-seq) (some empty? more))
     '()
     (cons (apply func (first a-seq) (apply-all first more))
           (apply my-map func (rest a-seq) (apply-all rest more))))))
