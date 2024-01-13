(ns app.server.stuff
  (:require [datascript.core :as d]))

(defonce !conn (d/create-conn {}))

;; TODO: What is this `db` here, and how is it set up in the
;;       `app.todo-list` namespace?

(defn create-item! [v]
  (d/transact! !conn
               [{:task/description v
                 :task/status :active}]))

(defn toggle-item! [id v]
  (d/transact! !conn
               [{:db/id id
                 :task/status (if v :done :active)}]))

(defn get-item [db id]
  (d/entity db id))


(defn todo-records [db]
  (->> (d/q '[:find [(pull ?e [:db/id :task/description]) ...]
              :where [?e :task/status]] db)
       (sort-by :task/description)))

(defn todo-count [db]
  (count
   (d/q '[:find [?e ...] :in $ ?status
          :where [?e :task/status ?status]] db :active)))
