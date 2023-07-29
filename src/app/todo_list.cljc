(ns app.todo-list
  (:require contrib.str
            #?(:clj [app.server.stuff :as server.stuff])
            [hyperfiddle.electric :as e]
            [hyperfiddle.electric-dom2 :as dom]
            [hyperfiddle.electric-ui4 :as ui]))

(e/def db) ; injected database ref; Electric defs are always dynamic

(e/defn TodoItem [id]
  (e/server
    (let [e (server.stuff/get-item db id)
          status (:task/status e)]
      (e/client
        (dom/div
          (ui/checkbox
           (case status :active false, :done true)
           (e/fn [v]
             (e/server
               (server.stuff/toggle-item! id v)
               nil))
           (dom/props {:id id}))
          (dom/label (dom/props {:for id}) (dom/text (e/server (:task/description e)))))))))

(e/defn InputSubmit [F]
; Custom input control using lower dom interface for Enter handling
  (e/client
    (dom/input (dom/props {:placeholder "Buy milk"})
               (dom/on "keydown" (e/fn [e]
                                   (when (= "Enter" (.-key e))
                                     (when-some [v (contrib.str/empty->nil (-> e .-target .-value))]
                                       (new F v)
                                       (set! (.-value dom/node) ""))))))))

(e/defn TodoCreate []
  (e/client
    (InputSubmit. (e/fn [v]
                    (e/server
                      (server.stuff/create-item! v)
                      nil)))))

(e/defn Todo-list [ring-request]
  (e/client
    (binding [dom/node js/document.body]
      (e/server
        (println "**** (server.stuff/!db-ref) =" (server.stuff/!db-ref))
        (binding [db (e/watch (server.stuff/!db-ref))]
          (e/client
            (dom/link (dom/props {:rel :stylesheet :href "/todo-list.css"}))
            (dom/h1 (dom/text "minimal todo list"))
            (dom/p (dom/text "it's multiplayer, try two tabs"))
            (dom/div (dom/props {:class "todo-list"})
                     (TodoCreate.)
                     (dom/div {:class "todo-items"}
                              (e/server
                                (if server.stuff/use-atom-for-db?
                                  (e/for-by :xxxx/id
                                      [{:keys [xxxx/id]} (server.stuff/todo-records db)]
                                    (TodoItem. id))
                                  (e/for-by :db/id
                                      [{:keys [db/id]} (server.stuff/todo-records db)]
                                    (TodoItem. id)))))
                     (dom/p (dom/props {:class "counter"})
                            (dom/span (dom/props {:class "count"})
                                      (dom/text (e/server (server.stuff/todo-count db))))
                            (dom/text " items left")))))))))
