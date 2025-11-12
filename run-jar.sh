#!/bin/bash

# AI Console - Run JAR Script
# Runs the standalone JAR file for the AI Console

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}AI Console - Running JAR${NC}"
echo -e "=============================="

# Check if JAR file exists
if [ ! -f "target/ai-console-1.0.0.jar" ]; then
    echo -e "${RED}Error: JAR file not found!${NC}"
    echo "Please build the project first: mvn clean package"
    exit 1
fi

# Check for API credentials
if [ -z "$CLIENT_KEY" ] && [ -z "$AZURE_CLIENT_KEY" ]; then
    echo -e "${YELLOW}Warning: No API credentials found!${NC}"
    echo "Please set your API credentials:"
    echo "  For OpenAI: export CLIENT_KEY='your-key-here'"
    echo "  For Azure OpenAI: export AZURE_CLIENT_KEY='your-key' and CLIENT_ENDPOINT='your-endpoint'"
    echo ""
    echo -e "${YELLOW}You can also copy .env.example to .env and configure your credentials there.${NC}"
    echo ""
fi

# Load environment variables from .env file if it exists
if [ -f ".env" ]; then
    echo -e "${GREEN}Loading environment variables from .env file...${NC}"
    set -o allexport
    source .env
    set +o allexport
fi

# Run the JAR
echo -e "${GREEN}Starting AI Console...${NC}"
echo ""
java -jar target/ai-console-1.0.0.jar