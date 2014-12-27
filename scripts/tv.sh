#!/bin/bash
source ../gradle.properties;
tmdb_id_for_newsroom=15621
curl -X GET https://api.themoviedb.org/3/tv/$tmdb_id_for_newsroom?api_key=$tmdbApiKey
