(ns minisql.core
  (:gen-class)
  (:require [clojure.tools.logging :as log]
            [clojure.data.json :as json]
            [integrant.core :as ig]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :refer [response status]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.resource :refer [wrap-resource]]
            [org.httpkit.server :as http]
            [nrepl.server :as nrepl]
            [com.walmartlabs.lacinia :refer [execute]]
            [minisql.graphql :as graphql]
            [minisql.db :as db]))


(defonce component (atom nil))

(def config
  (ig/read-string (slurp (clojure.java.io/resource "config.edn"))))



(defn wrap-internal-error [handler]
  (fn [req]
    (try
      (handler req)
      (catch Throwable t
        (log/error t)
        (-> (response (json/write-str
                       {:errors
                        {:message (or (.getMessage t) "Internal Server Error")}}))
            (status 500))))))




;; GraphQL Schema
(defmethod ig/init-key :http/graphql [_ {:keys [schema]}]
  (do
    (log/info "initializing schema")
    (graphql/init-schema schema)))


;; HTTP
(defmethod ig/init-key :http/server [_ {:keys [handler port]}]
  (let [server (http/run-server handler {:port port})]
    (log/info "HTTP server started on port" port)
    server))


(defmethod ig/halt-key! :http/server [_ server]
  (do
    (server :timeout 100)
    (log/info "HTTP server stopped")))



(defmethod ig/init-key :handler/app [_ {:keys [schema]}]
  (let [app (routes
             (GET "/" [] "Hello World!")
             (POST "/graphql" req
               (let [query (-> req
                               :body
                               slurp
                               (json/read-str :key-fn keyword)
                               :query)]
                 {:status 200
                  :headers {"Content-Type" "application/json"}
                  :body (json/write-str (execute schema query nil nil))}))
             (route/not-found "404 Not Found"))]

    (-> app
        (wrap-resource "build")
        (wrap-defaults
         (-> site-defaults
             (assoc-in [:security :anti-forgery] false)
             (assoc :session false)))
        wrap-internal-error)
    ))


;; nREPL
(defmethod ig/init-key :nrepl/server [_ {:keys [port]}]
  (when port
    (let [server (nrepl/start-server :port port)]
      (log/info "nREPL server started on port" port)
      server)))


(defmethod ig/halt-key! :nrepl/server [_ server]
  (do
    (nrepl/stop-server server)
    (log/info "HTTP server stopped")))


;; DB
(defmethod ig/init-key :db/init [_ _]
  (db/init))

;;Commands

(defn start! []
  (reset! component (ig/init config)))

(defn stop! []
  (when-not (nil? @component)
    (ig/halt! @component)
    (reset! component nil)))


(defn restart! []
  (do
    (stop!)
    (start!)))

(defn -main []
  (start!))
