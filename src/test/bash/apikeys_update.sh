#!/usr/bin/env bash

output="$(basename $(echo $0) | sed 's#sh#json#g' | sed 's#^#../resources/#g')"

curl --verbose \
    -X PUT \
    -H 'X-Contentful-Version: 5' \
    -H 'Content-Type: application/vnd.contentful.management.v1+json' \
    -H 'Authorization: Bearer '$CMA_TOKEN  \
    'https://api.contentful.com/spaces/'$SPACE_ID'/api_keys/5syMy6BsUtcVXlD6PpE6LA' \
    -d '{
        "name":"First API Key Name",
        "description":"Updated API Key Description",
        "environments":[
            {"sys":{"id":"master","linkType":"Environment","type":"Link"}},
            {"sys":{"id":"java_e2e","linkType":"Environment","type":"Link"}}
        ]
    }' \
    | sed 's/'${SPACE_ID}'/<space_id>/g' \
    | sed 's/'${CMA_TOKEN}'/<access_token>/g' \
    | sed 's/'${CDA_TOKEN}'/<access_token>/g' \
    | sed 's/'${USER_ID}'/<user_id>/g' \
    | tee ${output}


