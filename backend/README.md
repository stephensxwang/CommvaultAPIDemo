# Commvault APIs Backend Demo

> Demo on how to use java to call the Commvault REST APIs

## Build Setup

``` bash
# update the version numbers in each POM of a multi-module project
mvn --batch-mode release:update-versions -DdevelopmentVersion=x.x.x-SNAPSHOT

# update parent version for release
mvn -B versions:set -DnewVersion=1.0.0

# build for production
mvn package
```

For a detailed explanation on how things work, check out the [guide](http://documentation.commvault.com/commvault/) and [doc of Commvault Rest API](https://api.commvault.com/?version=latest).
