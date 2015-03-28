CREATE TABLE 'configuration' (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    created INTEGER NOT NULL,
    json TEXT NOT NULL
);

CREATE TABLE 'genres' (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    created INTEGER NOT NULL,
    json TEXT NOT NULL
);

CREATE TABLE 'show_summaries' (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    created INTEGER NOT NULL,
    tmdb_genre_id TEXT NOT NULL,
    json TEXT NOT NULL
);
