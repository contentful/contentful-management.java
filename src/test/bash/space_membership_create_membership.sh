#!/usr/bin/env bash

curl --verbose \
    -d '{"admin": true, "email": "mario+space_membership_test@contentful.com"}' \
    -H 'Content-Type: application/vnd.contentful.management.v1+json' \
    'https://api.contentful.com/spaces/'$SPACE_ID'/space_memberships?access_token='$CMA_TOKEN \
    | tee /tmp/create_space_membership_response
