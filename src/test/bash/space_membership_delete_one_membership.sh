#!/usr/bin/env bash

curl --verbose \
    -X DELETE \
    'https://api.contentful.com/spaces/'$SPACE_ID'/space_memberships/3oDvvmtZnw1DoMS56y1n5I?access_token='$CMA_TOKEN \
    | tee /tmp/delete_one_space_membership_response
