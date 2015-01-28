#!/bin/bash
source ../gradle.properties;
curl -X GET https://api.themoviedb.org/3/genre/tv/list?api_key=$tmdbApiKey | python -mjson.tool
