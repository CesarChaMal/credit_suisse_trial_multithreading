#!/usr/bin/env bash

#
# command line runner for the Credit Suisse Trial app
#

# mvn -DskipTests compile
mvn compile test

mvn jetty:run & SERVER_PID=$!

curl -i -X GET -H Accept:application/json "http://localhost:8080/credit_suisse_trial/"
CLIENT_PID=$!
