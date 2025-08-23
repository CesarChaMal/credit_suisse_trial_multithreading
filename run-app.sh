#!/usr/bin/env bash

#
# command line runner for the Credit Suisse Trial app
#

# Initialize SDKMAN
export SDKMAN_DIR="$HOME/.sdkman"
[[ -s "$SDKMAN_DIR/bin/sdkman-init.sh" ]] && source "$SDKMAN_DIR/bin/sdkman-init.sh"

sdk use java 8.0.462.fx-zulu

function cleanup() {
    if [ ! -z "$SERVER_PID" ]; then
        echo "Stopping server (PID: $SERVER_PID)..."
        kill -9 ${SERVER_PID} 2>/dev/null
    fi
}

trap cleanup EXIT

echo "Building and testing..."
mvn compile test -q

echo "Starting Jetty server..."
mvn jetty:run -q & SERVER_PID=$!

echo "Waiting for server to start..."
WAIT_COUNT=0
while ! nc -z localhost 8080 > /dev/null 2>&1; do
    if [ $WAIT_COUNT -eq 0 ]; then
        echo -n "Starting"
    fi
    echo -n "."
    sleep 2
    WAIT_COUNT=$((WAIT_COUNT + 1))
    if [ $WAIT_COUNT -gt 30 ]; then
        echo "\nServer failed to start after 60 seconds. Exiting."
        exit 1
    fi
done

echo "\nServer started! Testing endpoint..."
curl -s "http://localhost:8080/credit_suisse_trial/" > /dev/null
if [ $? -eq 0 ]; then
    echo "✓ Application is running at: http://localhost:8080/credit_suisse_trial/"
    echo "Press Ctrl+C to stop the server"
    wait $SERVER_PID
else
    echo "✗ Failed to connect to application"
    exit 1
fi
