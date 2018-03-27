(ns ^:figwheel-no-load theitconnection.app
  (:require [theitconnection.core :as core]
            [devtools.core :as devtools]))

(enable-console-print!)

(devtools/install!)

(core/init!)
