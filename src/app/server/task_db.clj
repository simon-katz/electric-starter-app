(ns app.server.task-db)

(defprotocol TasksDB
  (get-!db-ref [_])
  (create-item! [_ _v])
  (toggle-item! [_ _id _v])
  (get-item [_ _db _id])
  (todo-records [_ db])
  (todo-count [_ db]))
