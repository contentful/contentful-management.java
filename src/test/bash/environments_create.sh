#!/usr/bin/env bash

output="$(basename $(echo $0) | sed 's#sh#json#g' | sed 's#^#../resources/#g')"

curl --verbose \
    -X POST \
    -H 'Content-Type: application/vnd.contentful.management.v1+json' \
    -H 'Authorization: Bearer '$CMA_TOKEN  \
     -d '{"name":"environment_name"}' \
    "https://api.contentful.com/spaces/$SPACE_ID/environments" \
    | tee ${output}
