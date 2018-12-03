(ns aoc18.day-1
    (:require [clojure.java.io :as jio])
    (:require [clojure.string :as string]))

(defn puzzle-input
  []
  (->>
   "day_1.txt"
   jio/resource
   slurp
   string/split-lines
   (map #(Integer/parseInt %))))

(defn part-1
  []
  (println (reduce + 0 (puzzle-input))))

(defn part-2
  []
  (loop
   [freq 0
    changes (cycle (puzzle-input))
    prev (set '())]
    (if (contains? prev freq)
      (println freq)
      (recur (+ freq (first changes)) (rest changes) (conj prev freq)))))
