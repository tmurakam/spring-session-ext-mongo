#!/usr/bin/env sh

. ./bintray.config

# REST API
USER=tmurakam
NAME=spring-session-ext-mongo
VERSION=1.0.1

URL=https://api.bintray.com/gpg/$USER/maven/$NAME/versions/$VERSION

curl -v \
    -X POST \
    --user $USER:$APIKEY \
    -H "X-GPG-PASSPHRASE: $PASSPHRASE" \
    $URL
