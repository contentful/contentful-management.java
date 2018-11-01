#!/usr/bin/env bash

output="$(basename $(echo $0) | sed 's#sh#json#g' | sed 's#^#../resources/#g')"

curl --verbose \
    -X PUT \
    -H 'Content-Type: application/vnd.contentful.management.v1+json' \
    -H 'X-Contentful-Version: 2' \
    -H 'Authorization: Bearer '$CMA_TOKEN  \
    -d @../resources/rich_text_update_one_payload.json \
    'https://api.contentful.com/spaces/'$RICH_TEXT_SPACE_ID'/entries/6VkkamQ5zO4qWIugKkWowK' \
    | sed 's/'${RICH_TEXT_SPACE_ID}'/<space_id>/g' \
    | sed 's/'${CMA_TOKEN}'/<access_token>/g' \
    | sed 's/'${USER_ID}'/<user_id>/g' \
    | tee ${output}