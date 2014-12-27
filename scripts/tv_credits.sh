#!/bin/bash
source ../gradle.properties;
tmdb_id_for_newsroom=15621;
curl -X GET https://api.themoviedb.org/3/tv/$tmdb_id_for_newsroom/credits?api_key=$tmdbApiKey | python -mjson.tool
