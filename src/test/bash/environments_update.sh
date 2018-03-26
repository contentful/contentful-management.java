#!/usr/bin/env bash

output="$(basename $(echo $0) | sed 's#sh#json#g' | sed 's#^#../resources/#g')"

curl --verbose \
    -X PUT \
    -H 'Content-Type: application/vnd.contentful.management.v1+json' \
    -H 'Authorization: Bearer '$CMA_TOKEN  \
    -H 'X-Contentful-Version: 6' \
     -d '{"name":"environment_new_name"}' \
    "https://api.contentful.com/spaces/$SPACE_ID/environments/environment_id" \
    | tee ${output}
