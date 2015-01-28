#!/bin/bash
source ../gradle.properties;
genre_comedy=35;

curl -X GET "https://api.themoviedb.org/3/discover/tv?api_key=$tmdbApiKey&with_genres=$genre_comedy" | python -mjson.tool
