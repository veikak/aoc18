(ns aoc18.day-4
    (:require [clojure.java.io :as jio])
    (:require [clojure.string :as string]))

(defn parse-time [token]
  (let
   [[date time] (string/split token #" ")
    [year month day] (map #(Integer/parseInt %) (string/split date #"-"))
    [hour minute] (map #(Integer/parseInt %) (string/split time #":"))
    time-map {:year year :month month :day day :hour hour :minute minute}]
   {:time time-map}))

(defn parse-action [token]
  (let
   [words (string/split token #" ")
    first-word (first words)]
   (cond
     (= first-word "Guard")
     {:action :begin :id (Integer/parseInt (subs (second words) 1))}
     (= first-word "wakes")
     {:action :wake}
     (= first-word "falls")
     {:action :sleep})))

(defn parse-entry [entry-str]
  (let
   [time-t (subs entry-str 1 17)
    action-t (subs entry-str 19 (count entry-str))
    time (parse-time time-t)
    action (parse-action action-t)]
   (merge time action)))

(defn sort-entries [entries]
  (reduce
   (fn [acc k] (sort-by (fn [coll] (get-in coll [:time k])) acc))
   entries
   [:minute :hour :day :month :year]))

(defn puzzle-input []
  (->>
   "day_4.txt"
   jio/resource
   slurp
   string/split-lines
   (map parse-entry)
   sort-entries))

(defn minutes-asleep
  ([entries]
   (minutes-asleep entries nil nil {}))
  ([[entry & rest] id last-sleep minutes]
   (let
    [action (:action entry)]
    (cond
      (nil? entry)
      minutes
      (= action :begin)
      (recur rest (:id entry) nil minutes)
      (= action :sleep)
      (recur rest id (get-in entry [:time :minute]) minutes)
      (= action :wake)
      (let
       [guard-minutes (get minutes id [])]
        (let
         [minutes-asleep (range last-sleep (get-in entry [:time :minute]))]
          (recur rest id nil
                 (assoc minutes id (concat guard-minutes minutes-asleep)))))))))

(defn map-hash-map [f m]
  (zipmap (keys m) (map f (vals m))))

(defn part-1 []
  (let
   [minutes (->>
             (puzzle-input)
             minutes-asleep)
    time-asleep (->>
                 minutes
                 (map-hash-map count))
    most-asleep (first (apply max-key second (into [] time-asleep)))
    minute-freq (frequencies (get minutes most-asleep))
    most-likely-minute (first (apply max-key second (into [] minute-freq)))]
  (println (* most-asleep most-likely-minute))))

(defn part-2 []
  (let
   [most-asleep-by-id
    (->>
     (puzzle-input)
     minutes-asleep
     (map-hash-map frequencies)
     (map-hash-map #(apply hash-map (apply max-key second (into [] %)))))
    [sleepiest-id & [[sleepiest-min & _]]]
    (->>
     most-asleep-by-id
     (map-hash-map #(first (into [] %)))
     (into [])
     (sort-by #(second (second %)))
     last)]
   (println (* sleepiest-id sleepiest-min))))
