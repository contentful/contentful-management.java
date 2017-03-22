#!/usr/bin/env bash
if [ ! -e /tmp/contentful-upload-response ]; then
	echo 'please run `upload_upload.sh` first.'
	exit
else
	echo 'Upload found again'
fi

ID="$(grep -sirn '^    "id"' /tmp/contentful-upload-response | cut -d':' -f3 | cut -d'"' -f2)"
echo "ID: "$ID

curl \
	--request POST \
	--data '
{
  "fields":{
    "file":{
      "en-US":{
        "contentType":"image/jpeg",
        "uploadFrom":{
          "sys":{
  	    "id":"589W3nEu5ZTmmDY2gBoVcp", 
            "linkType":"Upload", 
            "type":"Link"
          }
        },
        "fileName":"upload"
      }
    },
    "description":{
      "en-US":"description"
    },
    "title":{
      "en-US":"title"
    }
  }
}' \
	--header 'Authorization: Bearer '$CMA_TOKEN \
	'https://api.contentful.com/spaces/'$SPACE_ID'/assets/' \
	| tee /tmp/contentful-create-asset-response

