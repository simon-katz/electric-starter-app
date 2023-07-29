(ns app.server.task-db-impls.datascript-db
  (:require
   [app.server.task-db :as task-db]
   [datascript.core :as d]))

(defrecord DatascriptTaskDB [!conn]
  task-db/TasksDB
  (get-!db-ref [_]
    !conn)
  (create-item! [_ v]
    (d/transact! !conn
                 [{:task/description v
                   :task/status :active}])
    nil)
  (toggle-item! [_ id v]
    (d/transact! !conn
                 [{:db/id id
                   :task/status (if v :done :active)}])
    nil)
  (get-item [_ db id]
    (d/entity db id))
  (todo-records [_ db]
    (->> (d/q '[:find [(pull ?e [:db/id :task/description]) ...]
                :where [?e :task/status]] db)
         (sort-by :task/description)))
  (todo-count [_ db]
    (count
     (d/q '[:find [?e ...] :in $ ?status
            :where [?e :task/status ?status]] db :active))))

(defonce the-db
  (->DatascriptTaskDB (d/create-conn {})))
