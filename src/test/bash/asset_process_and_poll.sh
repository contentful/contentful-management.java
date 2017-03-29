#!/usr/bin/env bash
if [ ! -e /tmp/contentful-create-asset-response ]; then
	echo 'please run `asset_create_asset_with_upload.sh` first.'
	exit
else
	echo 'Upload found again'
fi

ASSET_ID="$(grep -sirn '^    "id"' /tmp/contentful-create-asset-response | cut -d':' -f3 | cut -d'"' -f2)"
echo "ASSET_ID: "$ASSET_ID

curl \
	--verbose \
	--request PUT \
	--header 'Authorization: Bearer '$CMA_TOKEN \
	'https://api.contentful.com/spaces/'$SPACE_ID'/assets/'$ASSET_ID'/files/en-US/process'
echo polling

url="" 
attempts=10
while [ "$url" = "" -a $attempts -gt 0 ]; do 
	curl \
		--silent \
		--request GET \
		--header 'Authorization: Bearer '$CMA_TOKEN \
		'https://api.contentful.com/spaces/'$SPACE_ID'/assets/'$ASSET_ID \
		> /tmp/contentful-create-asset-process-poll-response

	url="$(grep -i 'url' /tmp/contentful-create-asset-process-poll-response | cut -d'"' -f4)" 
	echo "url: '$url', attempts left: '$((attempts--))'"

	if [ -z "$url" ]; then
		sleep 0.3
	fi
done

if [ -z "$url" ]; then
	echo 'No url found!'
else
	echo URL found: $url
fi

