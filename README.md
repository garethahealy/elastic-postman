[![Build Status](https://travis-ci.org/garethahealy/elastic-postman.svg?branch=master)](https://travis-ci.org/garethahealy/elastic-postman)
[![Release Version](https://img.shields.io/maven-central/v/com.garethahealy.elastic-postman/elastic-postman-parent.svg?maxAge=2592000)](https://mvnrepository.com/artifact/com.garethahealy.elastic-postman/elastic-postman-parent)
[![License](https://img.shields.io/hexpm/l/plug.svg?maxAge=2592000)]()

# elastic-postman
The idea of this project is to make it easier to search any GNU Mailman v2.
- https://www.gnu.org/software/mailman/

## Tech
- Apache Camel
- ElasticSearch
- Kibana

## What does it do?
1) Downloads a Mailman archive page
2) Finds all zip links
3) Downloads and unzips
4) Parses Mime content into a Map
5) Inserts map into ElasticSearch so that you can search via Kibana.

## Populate
If the mailing list is not already in ES or out-of-date, you can hit the following url:
- http://ip-of-elastic-postman:9001/elasticpostman?MailmanResourceUri={uri for mailing list you are interested in}

For instance, if you want to be able to search the sme-jon mailing list:
- https://post-office.corp.redhat.com/archives/sme-jon/

Then you would hit the following url:
- http://ip-of-elastic-postman:9001/elasticpostman?MailmanResourceUri=archives/sme-jon/

## Building the Project
To build firstly run:
- mvn clean install

If you want to run it locally, you can via:
- mvn camel:run

## Future...
- Create OpenShift build/deployment
