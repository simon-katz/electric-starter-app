((nil
  .
  ((cider-clojure-cli-aliases . "project/dev:user/dev:dev") ; TODO: Find a way to have "project/dev:user/dev" added separately. Maybe in dir-locals-2.el, using eval and something to add to `cider-clojure-cli-aliases`. Or maybe just in an Emacs init file, globally for all projects.
   (eval
    .
    (nomis/add-to-list-local 'cider-jack-in-nrepl-middlewares
                             "shadow.cljs.devtools.server.nrepl/middleware"
                             t))
   (eval
    .
    (nomis/add-to-list-local 'grep-find-ignored-files
                             "main.js")))))
