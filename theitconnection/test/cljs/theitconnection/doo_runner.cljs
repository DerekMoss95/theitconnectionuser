(ns theitconnection.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [theitconnection.core-test]))

(doo-tests 'theitconnection.core-test)

