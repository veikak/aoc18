(ns aoc18.day-2
    (:require [clojure.java.io :as jio])
    (:require [clojure.string :as string]))

(defn puzzle-input
  []
  (->> "day_2.txt"
       jio/resource
       slurp
       string/split-lines))

(defn count-appearances
  [n coll]
  (->> coll
       (filter #(some (partial = n) %))
       count))

(defn part-1
  []
  (def freqs
    (->>
     (puzzle-input)
     (map frequencies)
     (map vals)
     ))
  (def checksum (reduce * 1 (map #(count-appearances % freqs) [2 3])))
  (println checksum))
