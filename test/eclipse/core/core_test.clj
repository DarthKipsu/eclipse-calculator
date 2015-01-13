(ns eclipse.core.core-test
  (:require [clojure.test :refer :all]
            [eclipse.core.core :refer :all]))

(def att-interceptor
  {:state "attacker"})

(def def-interceptor
  {:state "defender"})

(deftest are-opponents-test
         (is (are-opponents att-interceptor def-interceptor))
         (is (are-opponents def-interceptor att-interceptor))
         (is (not (are-opponents def-interceptor def-interceptor)))
         (is (not (are-opponents att-interceptor att-interceptor))))

(deftest targets-for-test
         (is (= 1 (count (targets-for att-interceptor [def-interceptor att-interceptor]))))
         (is (= 2 (count (targets-for def-interceptor [def-interceptor att-interceptor att-interceptor]))))
         (is (= "defender" (:state (get (targets-for att-interceptor [def-interceptor att-interceptor]) 0))))
         (is (= "attacker" (:state (get (targets-for def-interceptor [def-interceptor att-interceptor]) 0)))))
