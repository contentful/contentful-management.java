#!/usr/bin/env bash

output="$(basename $(echo $0) | sed 's#sh$#json#g' | sed 's#^#../resources/#g')"

ENTRY_ID=34MftYTFmoOoKCqM48wcc8
SNAPSHOT_ID=3v5AOdklBfe7preLva5OeZ

curl --verbose \
    -X GET \
    -H 'Content-Type: application/vnd.contentful.management.v1+json' \
    -H 'Authorization: Bearer '$CMA_TOKEN  \
     'https://api.contentful.com/spaces/'$SPACE_ID'/entries/'$ENTRY_ID'/snapshots/'$SNAPSHOT_ID'' \
   | sed 's/'${SPACE_ID}'/<space_id>/g' \
    | sed 's/'${CMA_TOKEN}'/<access_token>/g' \
    | sed 's/'${USER_ID}'/<user_id>/g' \
    | tee ${output}
