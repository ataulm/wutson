#!/bin/bash
source ../gradle.properties;
curl --header "trakt-api-version: 2" --header "trakt-api-key: $traktApiKey" -X GET https://api-v2launch.trakt.tv/people/9739/movies?extended=full,images | python -mjson.tool
