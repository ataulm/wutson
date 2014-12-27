#!/bin/bash
source ../gradle.properties;
curl -X GET https://api.themoviedb.org/3/discover/tv?api_key=$tmdbApiKey
