#!/usr/bin/env bash

output="$(basename $(echo $0) | sed 's#sh$#json#g' | sed 's#^#../resources/#g')"

SNAPSHOT_ID=2fBAIYLYAgJcG0VRwg1MO7

curl --verbose \
    -X GET \
    -H 'Content-Type: application/vnd.contentful.management.v1+json' \
    -H 'Authorization: Bearer '$CMA_TOKEN  \
     'https://api.contentful.com/spaces/'$SPACE_ID'/content_types/'$CONTENT_TYPE_ID'/snapshots/'$SNAPSHOT_ID'' \
   | tee ${output}
