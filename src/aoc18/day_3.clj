(ns aoc18.day-3
    (:require [clojure.java.io :as jio])
    (:require [clojure.string :as string]))

(defn parse-pos [token]
  {:x (re-find #"\d*(?=,)" token)
   :y (re-find #"(?<=,)\d*" token)})

(defn parse-size [token]
  {:x (re-find #"\d*(?=x)" token)
   :y (re-find #"(?<=x)\d*" token)})

(defn parse-claim [claim]
  (let
   [[id-t _ pos-t size-t]
    (->
     claim
     (string/split #" "))
    id (subs id-t 1 (count id-t))
    pos (parse-pos pos-t)
    size (parse-size size-t)]
    {:id id
     :pos pos
     :size size}))

(defn puzzle-input []
  (->>
   "day_3.txt"
   jio/resource
   slurp
   string/split-lines
   (map parse-claim)))

(defn grid-insert
  [grid y x item]
  (let
   [row (get grid y {})
    items (get row x [])]
    (assoc grid y (assoc row x (conj items item)))))

(defn part-1 []
  (println (puzzle-input))
  (println (grid-insert (grid-insert {} 3 4 "foo") 3 4 "bar")))
