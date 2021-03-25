(ns minisql.db
  (:require [clojure.tools.logging :as log]
            [clojure.java.jdbc :as jdbc])
  (:import [java.io File]
           [java.sql DriverManager]))


(defonce MINISQL-DB
  (str (System/getenv "HOME") (File/separator) "minisql.db"))


(def db-spec
  {:classname "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname MINISQL-DB})



(defn- execute-ddl []

  (jdbc/db-do-commands
   db-spec
   (jdbc/create-table-ddl
    :databases
    [[:id "integer primary key autoincrement"]
     [:vendor "varchar(20) not null"]
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
  (when-not (.exists (File. MINISQL-DB))
    (log/debug "create database at" MINISQL-DB)
    (execute-ddl)))



(defn get-dbs [id]
  (jdbc/query db-spec
              (if (nil? id)
                ["select * from databases"]
                ["select * from databases where id =? " id])))


(defn add-db [{:keys [vendor] :as args}]
  (let [now (java.util.Date.)
        param (assoc args
                     :vendor (name vendor)
                     :created_at now
                     :updated_at now)
        id (-> (jdbc/insert! db-spec :databases param)
               first
               (get (keyword "last_insert_rowid()")))]
    (log/debug "inserted" id)
    (assoc param :id id)))


(defn update-db [{:keys [id] :as args}]
  (let [now (java.util.Date.)
        param (assoc args :updated_at args)
        id (-> (jdbc/update! db-spec param ["id=?" id])
               first)]
    (log/debug "update record (count:" id ")")
    param))



(defn rem-db [id]
  (-> (jdbc/delete! db-spec :databases ["id=?" id])
      first
      pos?))


(defn- get-catalog [^java.sql.DatabaseMetaData meta]
  (let [resultSet (-> meta .getCatalogs)]
    (loop [v []]
      (if (-> resultSet .next)
        (recur
         (conj v (.getString resultSet 1)))
        (first v)))))


(defn- get-columns [ meta cat schema table-name]
  (let [result (.getColumns meta cat schema table-name nil)]
    (loop [v []]
      (if (-> result .next)
        (recur
         (conj v {:name  (.toUpperCase (.getString result "COLUMN_NAME" ))
                  :nullable (.getBoolean result "NULLABLE")
                  :autoIncrement (.getBoolean result "IS_AUTOINCREMENT")
                  :typeName (.getString result "TYPE_NAME")
                  :dataType (.getString result "DATA_TYPE")
                  :size (.getInt result "COLUMN_SIZE")
                  :bufferLength (.getInt result "BUFFER_LENGTH")
                  }))
        v))))


(defn meta-query [{:keys [id package schema pojo sql name]}]
  (if-let [db (first (get-dbs id))]
    (let [conn (DriverManager/getConnection (:url db) (:username db) (:password db))
          meta (-> conn .getMetaData)
          cat (get-catalog meta)]

      {:name name
       :columns (get-columns meta cat schema name)})))
