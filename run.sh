#!/bin/bash

# Script to run the AI Console application
# Make sure to set your environment variables first

# Check if environment variables are set
if [ -z "$CLIENT_KEY" ] && [ -z "$AZURE_CLIENT_KEY" ]; then
    echo "⚠️  No API keys found!"
    echo "Please set either:"
    echo "  - CLIENT_KEY for OpenAI"
    echo "  - AZURE_CLIENT_KEY and CLIENT_ENDPOINT for Azure OpenAI"
    echo ""
    echo "Example:"
    echo "  export CLIENT_KEY=your_openai_api_key"
    echo "  export MODEL_ID=gpt-4o  # optional"
    echo ""
    exit 1
fi

echo "🚀 Starting AI Console..."
echo "Using Java: $(java -version 2>&1 | head -n 1)"

# Build and run the application
mvn compile exec:java