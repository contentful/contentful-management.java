#!/usr/bin/env bash

output="$(basename $(echo $0) | sed 's#sh$#json#g' | sed 's#^#../resources/#g')"

curl --verbose \
    -X POST \
    -H 'Content-Type: application/vnd.contentful.management.v1+json' \
    -H "X-Contentful-Content-Type: TESTID" \
    -H 'Authorization: Bearer '$CMA_TOKEN  \
    -d '{"fields":{"testField2":{"en-US":[2]}}}' \
    'https://api.contentful.com/spaces/'$SPACE_ID'/entries/' \
   | tee ${output}
