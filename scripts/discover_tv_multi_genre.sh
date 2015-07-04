#!/bin/bash
source ../gradle.properties;
genre_comedy=35;
genre_sci_fi=10765;
genre_animation=16;
genre_action=10759;
genre_drama=18;

curl -X GET "https://api.themoviedb.org/3/discover/tv?api_key=$tmdbApiKey&with_genres=$genre_comedy|$genre_sci_fi|$genre_animation|$genre_action|$genre_drama" | python -mjson.tool