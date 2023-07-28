((nil
  .
  ((cider-clojure-cli-aliases . ":dev")
   (eval
    .
    (nomis/add-to-list-local 'cider-jack-in-nrepl-middlewares
                             "shadow.cljs.devtools.server.nrepl/middleware"
                             t))
   (eval
    .
    (nomis/add-to-list-local 'grep-find-ignored-files
                             "main.js")))))
