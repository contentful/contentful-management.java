#!/usr/bin/env bash

output="$(basename $(echo $0) | sed 's#sh$#json#g' | sed 's#^#../resources/#g')"

ENTRY_ID=34MftYTFmoOoKCqM48wcc8

curl --verbose \
    -X GET \
    -H 'Content-Type: application/vnd.contentful.management.v1+json' \
    -H 'Authorization: Bearer '$CMA_TOKEN  \
     'https://api.contentful.com/spaces/'$SPACE_ID'/entries/'$ENTRY_ID'/snapshots' \
   | tee ${output}
