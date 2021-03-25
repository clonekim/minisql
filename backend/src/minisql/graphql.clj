(ns minisql.graphql
  (:require [clojure.edn :as edn]
            [com.walmartlabs.lacinia.util :refer [attach-resolvers]]
            [com.walmartlabs.lacinia.schema :as s]
            [minisql.db :as db]))



(defn list-db [ context {:keys [id]} _value]
  (db/get-dbs id))

(defn add-db [ context args _value]
  (db/add-db args))

(defn update-db [ context args _value]
  (db/update-db args))

(defn rem-db [context {:keys [id]} _value]
  (db/rem-db id))

(defn meta-query [context args _value]
  (db/meta-query args))


(defn init-schema [schema-name]
  (-> (clojure.java.io/resource schema-name)
      slurp
      edn/read-string
      (attach-resolvers
       {:list-db list-db
        :add-db add-db
        :update-db update-db
        :rem-db rem-db
        :meta-query meta-query
        })
      s/compile)
)
