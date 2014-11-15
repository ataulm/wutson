# Watson

Watson is a guide to what's on TV, backed by data from [The Movie Database (TMDb)][tmdb-link].

_This product uses the TMDb API but is not endorsed or certified by TMDb_

[tmdb-link]: http://www.themoviedb.org

# Workflow

As I'm mostly working on this myself, there won't be a PR process to get code into the main branch (`dev`). Instead, I'll work on "feature" branches, compare with `dev` and then merge the feature branch directly into `dev` when I'm happy. `master` branch will feature only releases - anything that is intended to go out for testing in an alpha/beta group or to production. Nothing should hit `master` that hasn't been in `dev` - that is, `master` should just be bookmarks of commits on `dev`, no cherry-picking or anything.
