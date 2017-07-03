#!/usr/bin/env bash

output="$(basename $(echo $0) | sed 's#sh#json#g' | sed 's#^#../resources/#g')"

curl --verbose \
    -X DELETE \
    -H 'Content-Type: application/vnd.contentful.management.v1+json' \
    -H 'X-Contentful-Version: 1' \
    -H 'Authorization: Bearer '$CMA_TOKEN  \
    'https://api.contentful.com/spaces/'$SPACE_ID'/extensions/3MdkEVBQvmAo4qU8egwyM8' \
    | tee ${output}
