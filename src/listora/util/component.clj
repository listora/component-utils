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

(defn verbose-start-system
  ([system]
     (verbose-start-system system (keys system)))
  ([system component-keys]
     (component/update-system system component-keys #'verbose-start)))

(defn verbose-stop-system
  ([system]
     (verbose-stop-system system (keys system)))
  ([system component-keys]
     (component/update-system-reverse system component-keys #'verbose-stop)))

(defrecord VerboseSystemMap []
  component/Lifecycle
  (start [system] (verbose-start-system system))
  (stop [system]  (verbose-stop-system system)))

(defmethod clojure.core/print-method VerboseSystemMap [_ w]
  (.write w "#<SystemMap>"))

(defn verbose-system-map [& {:as config}]
  (map->VerboseSystemMap config))
