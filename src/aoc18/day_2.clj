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

(defn sort-by-subs
  [end coll]
  (sort #(compare (subs %1 0 end) (subs %2 0 end)) coll))

(defn matching-indices
  [a b]
  (->>
     (map #(not= (compare %1 %2) 0) a b)
     (keep-indexed #(when (true? %2) %1))))

(defn diff-by-1
  [a b]
  (let [indices (matching-indices a b)]
    (when (= (count indices) 1)
      (first indices))))

(defn part-1
  []
  (let
   [freqs (->>
           (puzzle-input)
           (map frequencies)
           (map vals))
    checksum (reduce * 1 (map #(count-appearances % freqs) [2 3]))]
   (println checksum)))

(defn part-2
  []
  (let
   [in (puzzle-input)
    id-len (count (first in))
    sorted-by-subs (map #(sort-by-subs %1 in) (range id-len))]
   (loop
    [ids (reduce concat () sorted-by-subs)]
     (let [[first-id & [second-id & rest-ids]] ids]
       (if-let [diff-i (diff-by-1 first-id second-id)]
         (println (str
                   (subs first-id 0 diff-i)
                   (subs first-id (+ diff-i 1) (count first-id))))
         (recur rest-ids))))))
