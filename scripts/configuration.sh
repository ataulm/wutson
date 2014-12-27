#!/bin/bash
source ../gradle.properties;
curl -X GET https://api.themoviedb.org/3/configuration?api_key=$tmdbApiKey | python -mjson.tool
