#!/bin/bash

echo "usage: ./discover_tv.sh MY_VALID_TMDB_API_KEY";

curl -X GET https://api.themoviedb.org/3/discover/tv?api_key=$1
