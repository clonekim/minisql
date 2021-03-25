(defproject minisql "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/tools.cli "1.0.206"]
                 [org.clojure/tools.logging "1.1.0"]
                 [org.clojure/data.json "2.0.1"]
                 [org.clojure/java.jdbc "0.7.12"]
                 [integrant "0.8.0"]
                 [compojure "1.6.2"]
                 [ring/ring-core "1.9.2"]
                 [ring/ring-defaults "0.3.2"]
                 [ch.qos.logback/logback-classic "1.2.3"]
                 [org.xerial/sqlite-jdbc "3.6.20"]
                 [http-kit/http-kit "2.5.3"]
                 [com.walmartlabs/lacinia "0.38.0"]
                 [org.postgresql/postgresql "42.2.19"]
                 [com.oracle.database.jdbc/ojdbc8 "21.1.0.0"]]
  :repl-options {:init-ns minisql.core}
  :main minisql.core
  :profiles {:uberjar
             {:aot :all
              :omit-source true
              :uberjar-exclusions [#"META-INF/(leiningen|maven)"]}}
  )
