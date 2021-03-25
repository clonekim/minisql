(ns minisql.db
  (:require [clojure.tools.logging :as log]
            [clojure.java.jdbc :as jdbc])
  (:import [java.io File]
           [java.sql DriverManager ResultSet]))


(defonce MINISQL-DB
  (str (System/getenv "HOME") (File/separator) "minisql.db"))


(def db-spec
  {:classname "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname MINISQL-DB})



(defn- execute-ddl []
  "http://clojure-doc.org/articles/ecosystem/java_jdbc/using_ddl.html"

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
     [:queried_at :datetime]]))

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


(defn- get-columns [^java.sql.DatabaseMetaData meta cat schema table-name]
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
                  }))
        v))))


(defn meta-query [{:keys [id schema sql name]}]
  "id로 해당 데이터베이스를 연결하고
   스키마와 name(테이블명)으로 메타 정보를 가져온다"
  (if-let [db (first (get-dbs id))]
    (let [conn (DriverManager/getConnection (:url db) (:username db) (:password db))
          meta (-> conn .getMetaData)
          cat (get-catalog meta)]

      {:name name
       :columns (get-columns meta cat schema name)})))



(defn meta-query-sql [^java.sql.Connection conn sql]
  (let [statement (doto (.createStatement conn ResultSet/TYPE_FORWARD_ONLY ResultSet/CONCUR_READ_ONLY)
                        (.setFetchSize 20))
        meta (-> (doto (.executeQuery statement sql)
                       (.setFetchSize 20))
                 (.getMetaData))
        total (.getColumnCount meta)]

    (loop [v [] count 1]
      (if (>= total count)
        (recur
         (conj v {:name (.getColumnName meta count)
                  :nullable (.isNullable meta count)
                  :autoIncrement (.isAutoIncrement meta count)
                  :typeName (.getColumnTypeName meta count)
                  :dataType (.getColumnType meta count)
                  :size (.getColumnDisplaySize meta count)})
         (inc count))
        v))))
