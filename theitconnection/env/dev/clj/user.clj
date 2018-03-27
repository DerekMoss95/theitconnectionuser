(ns user
  (:require 
            [mount.core :as mount]
            [theitconnection.figwheel :refer [start-fw stop-fw cljs]]
            [theitconnection.core :refer [start-app]]))

(defn start []
  (mount/start-without #'theitconnection.core/repl-server))

(defn stop []
  (mount/stop-except #'theitconnection.core/repl-server))

(defn restart []
  (stop)
  (start))


