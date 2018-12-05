(ns aoc18.day-3
    (:require [clojure.java.io :as jio])
    (:require [clojure.string :as string]))

(defn parse-id [token]
  (->>
   (subs token 1 (count token))
   Integer/parseInt))

(defn parse-pos [token]
  {:x (Integer/parseInt (re-find #"\d*(?=,)" token))
   :y (Integer/parseInt (re-find #"(?<=,)\d*" token))})

(defn parse-size [token]
  {:x (Integer/parseInt (re-find #"\d*(?=x)" token))
   :y (Integer/parseInt (re-find #"(?<=x)\d*" token))})

(defn parse-claim [claim]
  (let
   [[id-t _ pos-t size-t]
    (->
     claim
     (string/split #" "))
    id (parse-id id-t)
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

(defn select-values [map ks]
  (reduce #(conj %1 (map %2)) [] ks))

(defn grid-insert [grid y x item]
  (let
   [row (get grid y {})
    items (get row x [])]
    (assoc grid y (assoc row x (conj items item)))))

(defn grid-cover-row [grid y from-x size item]
  (reduce #(grid-insert %1 y %2 item) grid (range from-x (+ from-x size))))

(defn grid-cover [grid [from-y from-x] [size-y size-x] item]
  (reduce #(grid-cover-row %1 %2 from-x size-x item) grid (range from-y (+ from-y size-y))))

(defn cover-claim [grid claim]
  (grid-cover grid
              (select-values (:pos claim) [:y :x])
              (select-values (:size claim) [:y :x])
              (:id claim)))

(defn find-non-overlapping [grid [claim & rest]]
  (let [{y :y x :x} (:pos claim)
        {y-size :y x-size :x} (:size claim)
        rows (select-values grid (range y (+ y y-size)))
        squares (->>
                 (map #(select-values % (range x (+ x x-size))) rows)
                 (apply concat))
        num-singles (count (filter #(= 1 (count %)) squares))]
    (if (= (count squares) num-singles)
      (:id claim)
      (recur grid rest))))

(defn part-1 []
  (->>
   (puzzle-input)
   (reduce cover-claim {})
   vals
   (map vals)
   (apply concat)
   (filter #(> (count %) 1))
   count
   println))

(defn part-2 []
  (let
   [claims (puzzle-input)
    grid (reduce cover-claim {} claims)]
    (println (find-non-overlapping grid claims))))
