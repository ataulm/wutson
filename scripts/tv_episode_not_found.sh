#!/bin/bash
source ../gradle.properties;
tmdb_id_for_gotham=60708;
season_number=1;
episode_number_that_does_not_exist=99;

curl -X GET https://api.themoviedb.org/3/tv/$tmdb_id_for_gotham/season/$season_number/episode/$episode_number_that_does_not_exist?api_key=$tmdbApiKey | python -mjson.tool
