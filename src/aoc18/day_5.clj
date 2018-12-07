(ns aoc18.day-5
  (:require [clojure.java.io :as jio])
  (:require [clojure.string :as string]))

(defn puzzle-input []
  (->>
   "day_5.txt"
   jio/resource
   slurp
   string/trim))

(defn zero-clamp [n]
  (if (< n 0) 0 n))

(defn reactive? [a b]
  (and (not= a b) (apply = (map #(string/lower-case %) [a b]))))

(defn first-reactive-pair
  ([polymer]
   (first-reactive-pair polymer 0))
  ([[head & rest] i]
   (when rest
     (if (reactive? head (first rest))
       i
       (recur rest (inc i))))))

(defn react-polymer
  ([polymer]
   (react-polymer polymer 0))
  ([polymer start]
   (if-let
    [sub-i (first-reactive-pair (subs polymer start))]
     (let
      [global-i (+ start sub-i)
       post-reaction
       (str (subs polymer 0 global-i)
            (subs polymer (+ global-i 2) (count polymer)))]
       (recur post-reaction (zero-clamp (dec (+ start sub-i)))))
     polymer)))

(defn part-1 []
  (->>
   (puzzle-input)
   react-polymer
   count
   println))

(defn remove-str-case-i [s r]
  (string/replace s (re-pattern (format "(?i)%s" r)) ""))

(defn part-2 []
  (let
   [in (puzzle-input)
    alphabet (apply str (map char (range 97 123)))
    variations (map #(remove-str-case-i in %) alphabet)]
   (->>
    variations
    (map react-polymer)
    (apply min-key count)
    count
    println)))
