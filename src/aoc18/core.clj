(ns aoc18.core
  (:gen-class)
  (:require [aoc18
             day-1
             day-2
             day-3
             day-4]))

(defn -main
  [& args]
  (if-let [day (ns-resolve *ns* (symbol (format "aoc18.day-%s/part-%s" (first args) (second args))))]
    (day)
    (println "Not implemented")))
