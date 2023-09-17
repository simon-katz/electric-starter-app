((nil
  .
  ((cider-clojure-cli-aliases . ":dev")
   (cider-preferred-build-tool . clojure-cli)
   ;; If you don't have `nomis/add-to-list-local`, you can copy it from
   ;; https://tinyurl.com/nomis-vgsn-el (or, if it's no longer there,
   ;; then from https://tinyurl.com/nomis-vgsn-2023-09-17-el).
   (eval
    .
    (nomis/add-to-list-local 'cider-jack-in-nrepl-middlewares
                             "shadow.cljs.devtools.server.nrepl/middleware"
                             t))
   (eval
    .
    (nomis/add-to-list-local 'grep-find-ignored-files
                             "main.js")))))
