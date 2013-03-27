(ns clojure-course-task02.core
  (:require [clojure.core.reducers :as r])
  (:gen-class))


(defn find-files [file-name path]
  (let [pattern (re-pattern file-name)
        files (file-seq (clojure.java.io/file path))]
    (r/filter #(not (nil? %))
              (r/map #(re-find pattern (.getName %)) files))))

(defn usage []
  (println "Usage: $ run.sh file_name path"))

(defn print-file
  ([] nil)
  ([_ file-name] (println file-name)))

(defn -main [file-name path]
  (if (or (nil? file-name)
          (nil? path))
    (usage)
    (do
      (println "Searching for " file-name " in " path "...")
      (r/fold print-file (find-files file-name path)))))
