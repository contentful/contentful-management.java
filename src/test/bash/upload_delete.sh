#!/usr/bin/env bash
if [ ! -e /tmp/contentful-upload-response ]; then
	echo 'please run `upload.sh` first.'
	exit
else
	echo 'Not creating asset again'
fi

ID="$(grep -sirn '^    "id"' /tmp/contentful-upload-response | cut -d':' -f3 | cut -d'"' -f2)"
echo "ID: "$ID

set -ev
curl \
	--verbose \
	--request DELETE \
	--header 'Authorization: Bearer '$CMA_TOKEN \
	'https://upload.contentful.com/spaces/'$SPACE_ID'/uploads/'$ID \
	| tee /tmp/contentful-upload-delete-response

rm /tmp/contentful-upload-response
