(ns theitconnection.db.core
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.operators :refer :all]
            )
  (:import org.bson.types.ObjectId [com.mongodb MongoOptions ServerAddress]))

(defonce conn (mg/connect))
(defonce db (mg/get-db conn "users"))

(defn save-message [params]
  (mc/insert db "customers" params))

(defn get-messages []
  (mc/find-maps db "customers"))

(defn get-message [id-string]
  (mc/find-one db "customers" {:_id (ObjectId. id-string)}))

(defn get-message-by-email [email]
  (mc/find-one db "customers" {:email email}))

(defn login [params]
  (mc/find-one db "customers" {:email params :password params}))
