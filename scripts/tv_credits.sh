#!/bin/bash
source ../gradle.properties;
tmdb_id_for_the_arrow=1412-arrow;
curl -X GET https://api.themoviedb.org/3/tv/$tmdb_id_for_the_arrow/credits?api_key=$tmdbApiKey | python -mjson.tool
