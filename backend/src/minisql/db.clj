(ns minisql.db
  (:require [clojure.tools.logging :as log]
            [clojure.java.jdbc :as jdbc])
  (:import [java.io File]))


(defonce MINISQL-DB
  (str (System/getenv "HOME") (File/separator) "minisql.db"))


(def db-spec
  {:classname "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname MINISQL-DB})



(defn create-db []

  (jdbc/db-do-commands
   db-spec
   (jdbc/create-table-ddl
    :databases
    [[:id "integer primary key autoincrement"]
     [:vender "varchar(20) not null"]
     [:url "varchar(100) not null"]
     [:username "varchar(100) not null"]
     [:password "varchar(100) not null"]
     [:created_at :datetime]
     [:updated_at :datetime]]))


   (jdbc/db-do-commands
   db-spec
   (jdbc/create-table-ddl
    :sql_logs
    [[:id "integer primary key autoincrement"]
     [:sql :text]
     [:created_at :datetime]]))

  )

(defn init []
  (if-not (.exits (File. MINISQL-DB))
    (log/debug "create database at" MINISQL-DB)
    (create-db)))



(defn get-connections [id]
  (if (nil? id)
    (jdbc/query db-spec
                (if (nil? id)
                  ["select * from databases"]
                  ["select * from databases where id =? " id]))))


(defn add-connection [args]
  (let [now (java.util.Date.)
        params (assoc args :created_at now :updated_at now)
        id (-> (jdbc/insert! db-spec :databases params)
               first
               (keyword "last_insert_id()"))]

    (assoc params :id id)))


(defn update-connection [{:keys [id] :as args}]
  (let [now (java.util.Date.)
        param (assoc args :updated_at args)
        id (-> (jdbc/update! db-spec param ["id=?" id])
               first)]
    param))



(defn remove-connection [id]
  (-> (jdbc/delete! db-spec :databases ["id=?" id])
      first
      pos?))
