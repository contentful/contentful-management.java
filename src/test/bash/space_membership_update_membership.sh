#!/usr/bin/env bash

curl --verbose \
    -X PUT \
    -d '{"admin": false}' \
    -H 'Content-Type: application/vnd.contentful.management.v1+json' \
    -H 'Authorization: Bearer '$CMA_TOKEN \
    -H 'X-Contentful-Version: 1' \
    'https://api.contentful.com/spaces/'$SPACE_ID'/space_memberships/7CgvT6bSNvpojgcdETZvL0' \
    | tee /tmp/update_space_membership_response

