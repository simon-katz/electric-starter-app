(ns app.server.server
  (:require
   [app.server.task-db :as task-db]
   [app.server.task-db-impls.clojure-atom-db :as atom-db]
   [app.server.task-db-impls.datascript-db :as datascript-db]))

(def use-atom-for-db? (let [v false]
                        (println "**** Setting use-atom-for-db? to" v)
                        v))

(defn !the-db []
  (println "**** !the-db -- use-atom-for-db? =" use-atom-for-db?)
  (if use-atom-for-db?
    atom-db/the-db
    datascript-db/the-db))

;; TODO: Copy this to a new repo where the DB is a set of rules and
;;       events, and have at it!

(defn !db-ref []
  (task-db/get-!db-ref (!the-db)))

(defn create-item! [v]
  (task-db/create-item! (!the-db) v)
  nil)

(defn toggle-item! [id v]
  (task-db/toggle-item! (!the-db) id v)
  nil)

(defn get-item [db id]
  (task-db/get-item (!the-db) db id))

(defn todo-records [db]
  (task-db/todo-records (!the-db) db))

(defn todo-count [db]
  (println "**** db =" db)
  (task-db/todo-count (!the-db) db))
