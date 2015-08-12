#!/bin/bash
source ../gradle.properties;
curl -X GET "https://api.themoviedb.org/3/search/tv?api_key=$tmdbApiKey&query=break" | python -mjson.tool
