(ns theitconnection.app
  (:require [theitconnection.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
