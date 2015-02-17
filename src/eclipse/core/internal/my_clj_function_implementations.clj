(ns eclipse.core.internal.my-clj-function-implementations)

(defn my-get-in
  "takes a map and a vector containing a path inside that map as a parameter and
  returns the value nested inside the sequence of keys in the given vector."
  [a-map key-seq]
  (loop [acc a-map i 0]
    (if (= i (count key-seq))
      acc
      (recur ((key-seq i) acc) (inc i)))))

(defn my-repeat
  "takes a value of any type and a multiplier and returns a lazy sequence with
  the value repeated by the multiplier."
  ([n value] (my-repeat n value '()))
  ([n value a-seq]
   (if (zero? n)
     a-seq
     (recur (dec n) value (conj a-seq value)))))

(defn to-vector
  "takes a sequence and returns a vector containing all the elements in the seq."
  [a-seq]
  (if (vector? a-seq) a-seq (vec a-seq)))

(defn my-first
  "takes a sequence and returns the first element in it."
  [a-seq]
  (let [a-vec (to-vector a-seq)]
    (a-vec 0)))

(defn my-rest
  "takes a sequence and returns a new vector with all but the first element of
  the given sequence"
  [a-seq]
  (let [a-vec (to-vector a-seq)]
    (loop [acc [] n 1]
      (if (== n (count a-seq))
        acc
        (recur (conj acc (a-vec n)) (inc n))))))

(defn apply-all 
  "a helper function for my-map that takes a function and a sequence, goes through
  the sequence and applies the function to each value and then returns the results
  of the function in a sequence."
  [func a-seq]
  (persistent! (reduce (fn [acc n] (conj! acc (func n))) (transient []) a-seq)))
  
(defn my-some
  "takes a predicate function and a sequence and returns true when first of the
  sequences values meets the predicate and nil if none will."
  [func [seq-first & seq-rest]]
  (when seq-first (or (func seq-first) (recur func seq-rest))))

(defn my-map
  "takes a function and one or more sequences and returns a vector containing
  the results of applying the function to the first set of parameters, then the 
  second and so on until reaching the end of one or more of the sequences."
  ([func a-seq] (apply-all func a-seq))
  ([func a-seq & more]
   (if (or (empty? a-seq) (my-some empty? more))
     []
     (apply vector (apply func (my-first a-seq) (apply-all my-first more))
            (apply my-map func (my-rest a-seq) (apply-all my-rest more))))))

(defn my-range
  "returns a seq containing numbers starting from 0 until the number (exclusive)
  given, or starting from the first parameter (inclusive) and ending before the
  second one."
  ([end] (my-range 0 end))
  ([start end]
   (loop [acc '() n (dec end)]
     (if (== n (dec start)) acc
       (recur (conj acc n) (dec n))))))

(defn my-reduce
  "takes a function and a list as parameters and returns the list reduced to a
  single value based on the given function. Optional second parameter (before
  the list) is a beginning value for the operation. The function given must
  take two parameters."
  ([func [seq-first & seq-rest]]
   (if (empty? seq-rest)
     (if seq-first seq-first 0)
     (my-reduce func seq-first seq-rest)))
  ([func value [seq-first & seq-rest]]
   (if (empty? seq-rest)
     (if seq-first (func value seq-first) value)
     (recur func (func value seq-first) seq-rest))))
