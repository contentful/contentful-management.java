#!/usr/bin/env bash

curl --verbose \
    'https://api.contentful.com/spaces/'$SPACE_ID'/space_memberships?access_token='$CMA_TOKEN \
    | tee /tmp/get_all_space_membership_response
