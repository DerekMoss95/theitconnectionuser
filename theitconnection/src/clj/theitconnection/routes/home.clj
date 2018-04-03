
(ns theitconnection.routes.home
  (:require [theitconnection.layout :as layout]
            [bouncer.core :as b]
            [bouncer.validators :as v]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [theitconnection.db.core :as db]))

(defn home-page []
  (layout/render "home.html"))

(defn validate-message [params]
  (first
   (b/validate
    params
    :firstname v/required
    :lastname v/required
    :email v/required
    :phone v/required
    :password v/required
    )))

(defn save-message [req]
  (db/save-message
   (:params req))
  (response/ok "Message saved"))

(defn login [req]
  (db/login
   (:params req))
  (response/ok "Successful login"))

(defroutes home-routes
  (GET "/" []
       (home-page))
  (GET "/docs" []
       (-> (response/ok (-> "docs/docs.md" io/resource slurp))
           (response/header "Content-Type" "text/plain; charset=utf-8")))
  (POST "/register" req
        (response/ok (save-message req)))
  (POST "/login" [email password]
        (when-let [email (db/login email password)]
          (response/ok (str email "Logged in")))))

