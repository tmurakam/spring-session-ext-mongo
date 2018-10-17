#!/usr/bin/env sh
# call bintray GPG sign REST API

. ./bintray.config

USER=tmurakam
NAME=spring-session-ext-mongo
VERSION=2.1.1

URL=https://api.bintray.com/gpg/$USER/maven/$NAME/versions/$VERSION

curl -v \
    -X POST \
    --user $USER:$APIKEY \
    -H "X-GPG-PASSPHRASE: $PASSPHRASE" \
    $URL
