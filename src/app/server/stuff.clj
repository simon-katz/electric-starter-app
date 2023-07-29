(ns app.server.stuff
  (:require
   [com.nomistech.clj-utils :as clj-utils]
   [datascript.core :as d]
   [medley.core :as medley]))

(def use-atom-for-db? true)

(defonce !atom-db (atom []))

(defonce !conn (d/create-conn {}))

;; TODO: What is this `db` here, and how is it set up in the
;;       `app.todo-list` namespace?
;;       - Oh! See the binding of `db` in `app.todo-list`.\
;;       - `e/watch` creates a reactive value from a reference.

;; TODO: Copy this to a new repo where the DB is a set of rules and
;;       events, and have at it!

(defn !db-ref []
  (if use-atom-for-db?
    !atom-db
    !conn))

(defn create-item! [v]
  (if use-atom-for-db?
    (swap! !atom-db conj {:xxxx/id          (java.util.UUID/randomUUID)
                          :task/description v
                          :task/status      :active})
    (d/transact! !conn
                 [{:task/description v
                   :task/status :active}])))

(defn toggle-item! [id v]
  (if use-atom-for-db?
    (let [old-val @!atom-db
          pos     (clj-utils/position #(= id (:xxxx/id %))
                                      old-val)
          new-val (assoc-in old-val [pos :task/status] (if v :done :active))]
      (compare-and-set! !atom-db old-val new-val))
    (d/transact! !conn
                 [{:db/id id
                   :task/status (if v :done :active)}])))

(defn get-item [db id]
  (if use-atom-for-db?
    (medley/find-first #(= id (:xxxx/id %))
                       db)
    (d/entity db id)))


(defn todo-records [db]
  (let [items (if use-atom-for-db?
                db
                (d/q '[:find [(pull ?e [:db/id :task/description]) ...]
                       :where [?e :task/status]] db))]
    (->> items
         (sort-by :task/description))))

(defn todo-count [db]
  (println "**** db =" db)
  (if use-atom-for-db?
    (count (filter #(= :active (:task/status %)) db))
    (count
     (d/q '[:find [?e ...] :in $ ?status
            :where [?e :task/status ?status]] db :active))))
