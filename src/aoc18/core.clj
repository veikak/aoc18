(ns aoc18.core
  (:gen-class))

(require '(aoc18 day-1))

(defn -main
  [& args]
  (if-let [day (ns-resolve *ns* (symbol (format "aoc18.day-%s/part-%s" (first args) (second args))))]
    (day)
    (println "Not implemented")))
