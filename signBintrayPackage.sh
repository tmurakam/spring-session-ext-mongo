#!/usr/bin/env sh
# call bintray GPG sign REST API

. ./bintray.config

if [ $# -ne 1 ]; then
    echo "Usage: $0 <version>"
    exit 0
fi

USER=tmurakam
NAME=spring-session-ext-mongo
VERSION=$1

echo "Signing version $VERSION..."

URL=https://api.bintray.com/gpg/$USER/maven/$NAME/versions/$VERSION

curl -v \
    -X POST \
    --user $USER:$APIKEY \
    -H "X-GPG-PASSPHRASE: $PASSPHRASE" \
    $URL
