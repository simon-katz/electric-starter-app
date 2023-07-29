(ns app.server.task-db-impls.clojure-atom-db
  (:require
   [app.server.task-db :as task-db]
   [com.nomistech.clj-utils :as clj-utils]
   [medley.core :as medley]))

(defrecord ClojureAtomTaskDB [!atom-db]
  task-db/TasksDB
  (get-!db-ref [_]
    !atom-db)
  (create-item! [_ v]
    (swap! !atom-db conj {:xxxx/id          (java.util.UUID/randomUUID)
                          :task/description v
                          :task/status      :active})
    nil)
  (toggle-item! [_ id v]
    (let [old-val @!atom-db
          pos     (clj-utils/position #(= id (:xxxx/id %))
                                      old-val)
          new-val (assoc-in old-val [pos :task/status] (if v :done :active))]
      (compare-and-set! !atom-db old-val new-val))
    nil)
  (get-item [_ db id]
    (medley/find-first #(= id (:xxxx/id %))
                       db))
  (todo-records [_ db]
    (->> db
         (sort-by :task/description)))
  (todo-count [_ db]
    (count (filter #(= :active (:task/status %)) db))))

(defonce the-db
  (->ClojureAtomTaskDB (atom [])))
