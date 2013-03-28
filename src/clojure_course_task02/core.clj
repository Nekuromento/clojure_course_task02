(ns clojure-course-task02.core
  (:require [clojure.core.reducers :as r])
  (:import java.io.File)
  (:gen-class))

(set! *warn-on-reflection* true)

(defn file-seq-r [file]
  (let [content (.listFiles ^File file) ;on big folders this can cause out of memory error
        files (filter (memfn ^File isFile) content)
        dirs (r/filter (memfn ^File isDirectory) content)
        dirs-content (r/foldcat (r/mapcat file-seq-r dirs))]
    (r/cat files dirs-content)))

(defn find-files [file-name path]
  (let [pattern (re-pattern file-name)
        files (file-seq-r (clojure.java.io/file path))]
    (r/filter #(not (nil? %))
              (r/map #(re-find pattern (.getName ^File %)) files))))

(defn usage []
  (println "Usage: $ run.sh file_name path"))

(defn print-file
  ([]
   nil)
  ([_ file-name]
   (if (not (nil? file-name))
     (println file-name))))

(defn -main [file-name path]
  (if (or (nil? file-name)
          (nil? path))
    (usage)
    (do
      (println "Searching for " file-name " in " path "...")
      (r/fold print-file (find-files file-name path)))))
