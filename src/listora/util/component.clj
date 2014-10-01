(ns listora.util.component
  (:require [clojure.string :as str]
            [com.stuartsierra.component :as component]
            [taoensso.timbre :as log]))

(defn component-name [component]
  (-> component class .getName (str/split #"\.") last))

(defn verbose-start [component]
  (log/info (str "Starting " (component-name component) "…"))
  (component/start component))

(defn verbose-stop [component]
  (log/info (str "Stopping " (component-name component) "…"))
  (component/stop component))
