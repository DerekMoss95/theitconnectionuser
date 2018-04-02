(ns theitconnection.core
  (:require [reagent.core :as r :refer [atom]]
            [reagent.session :as session]
            [reagent.crypt :as crypt]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            ;[theitconnection.ajax :refer [load-interceptors!]]
            [cljsjs.react :as react]
            [ajax.core :refer [GET POST]])
  (:import goog.History))

(defonce session (r/atom {:page :home}))

 (defn about-page []
   [:div.container
    [:div.row
     [:div.col-md-12 "Welcome! You are logged in!"]]])

(defn send-form! [FIELDS]
  (POST "/register"
        {:params @FIELDS
         :handler #(secretary/dispatch! "#/about")
         :error-handler #(.error js/console (str "error:" %))}))

(defn send-login! [FIELDS]
  (POST "/login"
        {:params @FIELDS
         :handler #(secretary/dispatch! "#/about")
         :eror-handler #(.error js/console (str "error:" %))}))

(defn register-form []
  (let [FIELDS (atom {})]
    (fn []
    [:div.content
     [:div.form.group
      [:p "First Name: "
       [:input.form-control
        {:type :text
         :firstname :firstname
         :on-change #(swap! FIELDS assoc :firstname (-> % .-target .-value))
         :value (:firstname @FIELDS)}]]
      [:p "Last Name: "
       [:input.form-control
        {:type :text
         :lastname :lastname
         :on-change #(swap! FIELDS assoc :lastname (-> % .-target .-value))
         :value (:lastname @FIELDS)}]]
      [:p "Email: "
       [:input.form-control
        {:type :email
         :email :email
         :on-change #(swap! FIELDS assoc :email (-> % .-target .-value))
         :value (:email @FIELDS)}]]
      [:p "Phone Number: "
       [:input.form-control
        {:type :text
         :phone :phone
         :on-change #(swap! FIELDS assoc :phone (-> % .-target .-value))
         :value (:phone @FIELDS)}]]
      [:p "Password: "
       [:input.form-control
        {:type :password
         :password :password
         :on-change #(swap! FIELDS assoc :password (-> % .-target .-value))
         :value (:password @FIELDS)}]]
      [:input.btn.btn-primary {:type :submit :on-click #(send-form! FIELDS) :value "Submit"}]]])))

(defn login-form []
  (let [FIELDS (atom {})]
    (fn []
    [:div.content
     [:div.form.group
      [:p "Email: "
       [:input.form-control
        {:type :text
         :email :email
         :on-change #(swap! FIELDS assoc :email (-> % .-target .-value))
         :value (:email @FIELDS)}]]
      [:p "Password: "
       [:input.form-control
        {:type :password
         :password :password
         :on-change #(swap! FIELDS assoc :password (-> % .-target .-value))
         :value (:password @FIELDS)}]]
      [:input.btn.btn-primary {:type :submit :on-click #(send-login! FIELDS) :value "Submit"}]]])))


(defn register-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     [register-form]]]])

 (defn login-page []
   [:div.container
    [:div.row
     [:div.col-md-12
      [login-form]]]])

(defn home-page []
  [:div.container
   (when-let [docs (:docs @session)]
     [:div.row>div.col-sm-12
      [:div {:dangerouslySetInnerHTML
             {:__html (md->html docs)}}]])])

(def pages
  {:home #'home-page
   :about #'about-page
   :register #'register-page
   :login #'login-page
   })

(defn page []
  [(pages (:page @session))])

(defn nav-link [uri title page]
  [:li.nav-item
   {:class (when (= page (:page @session)) "active")}
   [:a.nav-link {:href uri} title]])

(defn navbar []
  [:nav.navbar.navbar-dark.bg-primary.navbar-expand-md
   {:role "navigation"}
   [:button.navbar-toggler.hidden-sm-up
    {:type "button"
     :data-toggle "collapse"
     :data-target "#collapsing-navbar"}
    [:span.navbar-toggler-icon]]
   [:a.navbar-brand {:href "#/"} "theitconnection"]
   [:div#collapsing-navbar.collapse.navbar-collapse
    [:ul.nav.navbar-nav.mr-auto
     [nav-link "#/" "Home" :home]
     ;[nav-link "#/about" "About" :about]
     [nav-link "#/register" "Register" :register]
     [nav-link "#/login" "Login" :login]]]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (swap! session assoc :page :home))

 (secretary/defroute "/about" []
   (swap! session assoc :page :about))

(secretary/defroute "/login" []
  (swap! session assoc :page :login))

(secretary/defroute "/register" []
  (swap! session assoc :page :register))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
        (events/listen
          HistoryEventType/NAVIGATE
          (fn [event]
              (secretary/dispatch! (.-token event))))
        (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn fetch-docs! []
  (GET "/docs" {:handler #(swap! session assoc :docs %)}))

(defn mount-components []
  (r/render [#'navbar] (.getElementById js/document "navbar"))
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  ;(load-interceptors!)                  
  (fetch-docs!)
  (hook-browser-navigation!)
  (mount-components))
