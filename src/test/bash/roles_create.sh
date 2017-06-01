#!/usr/bin/env bash

output="$(basename $(echo $0) | sed 's#sh#json#g' | sed 's#^#../resources/#g')"

curl --verbose \
    -X POST \
    -H 'Content-Type: application/vnd.contentful.management.v1+json' \
    -H 'Authorization: Bearer '$CMA_TOKEN  \
    -d @../resources/roles_create_payload.json \
    'https://api.contentful.com/spaces/'$SPACE_ID'/roles' \
    | tee ${output}
