#!/usr/bin/env bash
if [ ! -e /tmp/200.jpg ]; then
	echo 'Downloading cat'
	curl https://http.cat/200 > /tmp/200.jpg
else
	echo 'Not downloading cat again.'
fi

set -ev
curl \
	--request POST \
	--header 'Content-Type: application/octet-stream' \
	--header 'Authorization: Bearer '$CMA_TOKEN \
	--data-binary @/tmp/200.jpg \
	'https://upload.contentful.com/spaces/'$SPACE_ID'/uploads' \
	| tee /tmp/contentful-upload-response

