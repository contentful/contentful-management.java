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
	--request GET \
	--header 'Authorization: Bearer '$F_CMA_TOKEN \
	'https://upload.contentful.com/spaces/'$F_SPACE_ID'/uploads/'$ID \
	| tee /tmp/contentful-upload-id-response

