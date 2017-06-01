#!/usr/bin/env bash

curl --verbose \
    'https://api.contentful.com/spaces/'$SPACE_ID'/space_memberships/7mgt3wsJ8oU6cbI2XQL12h?access_token='$CMA_TOKEN \
    | tee /tmp/get_one_space_membership_response
