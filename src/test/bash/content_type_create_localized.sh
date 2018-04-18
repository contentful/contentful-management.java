#!/usr/bin/env bash

output="$(basename $(echo $0) | sed 's#sh#json#g' | sed 's#^#../resources/#g')"

curl --verbose \
    -X POST \
    -H 'Content-Type: application/vnd.contentful.management.v1+json' \
    -H 'Authorization: Bearer '$CMA_TOKEN  \
    -d @../resources/content_type_create_localized_payload.json \
    'https://api.contentful.com/spaces/'$SPACE_ID'/content_types' \
    | sed 's/'${SPACE_ID}'/<space_id>/g' \
    | sed 's/'${CMA_TOKEN}'/<access_token>/g' \
    | sed 's/'${USER_ID}'/<user_id>/g' \
    | tee ${output}
