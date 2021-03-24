(ns minisql.graphql
  (:require [clojure.edn :as edn]
            [com.walmartlabs.lacinia.util :refer [attach-resolvers]]
            [com.walmartlabs.lacinia.schema :as s]
            [minisql.db :as db]))



(defn get-connections [ context {:keys [id]} _value]
  (db/get-connections id))

(defn update-connection [ context args _value]
  (db/update-connection args))

(defn remove-connection [ context {:keys [id]}_value]
  (db/remove-connection id))


(defn init-schema [schema-name]
  (-> (clojure.java.io/resource schema-name)
      slurp
      edn/read-string
      (attach-resolvers
       {:getConnections get-connections
        :updateConnection update-connection
        :removeConnection remove-connection
        })
      s/compile)
)
