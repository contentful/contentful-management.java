#!/usr/bin/env bash

output="$(basename $(echo $0) | sed 's#sh#json#g' | sed 's#^#../resources/#g')"

curl --verbose \
    -X GET \
    -H 'Content-Type: application/vnd.contentful.management.v1+json' \
    -H 'Authorization: Bearer '$CMA_TOKEN  \
    'https://api.contentful.com/users/me/access_tokens/0LYG3uP7KihkEwsRsYijTo' \
    | tee ${output}
