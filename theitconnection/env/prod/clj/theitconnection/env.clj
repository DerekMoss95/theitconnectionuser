(ns theitconnection.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[theitconnection started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[theitconnection has shut down successfully]=-"))
   :middleware identity})
