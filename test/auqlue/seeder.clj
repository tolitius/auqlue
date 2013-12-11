(ns auqlue.seeder
  (:require [clojure.edn :as edn])
  (:use auqlue.dbnator
        clojure.tools.logging
        [datomic.api :only (db) :as d]))

(defn read-datomic-edn [path]
  (edn/read-string {:readers *data-readers*} 
                   (slurp path)))

(defn create-schema [part schema] 
  (d/transact *conn* 
    (read-datomic-edn part))     ;; creates a custom parition
  (d/transact *conn* 
    (read-datomic-edn schema)))

(defn create-auqlue-schema [uri]
  (connect-to uri)
  (create-schema "resources/db/partition.edn" 
                 "resources/db/schema.edn"))

(defn data-from [uri here]
  (connect-to uri)
  (create-schema "resources/db/partition.edn" 
                 "resources/db/schema.edn")
  (d/transact *conn*
    ;; needs an 'eval' to process all the BigInts and friends
    (eval (do (use '[datomic.api :only (db) :as d]) (read-datomic-edn here))))
  (info "data from \"" here "\" loaded to [" uri "]"))

(defn delete-test-db [uri]
  (d/delete-database uri)
  (info "database [" uri "] is deleted"))
