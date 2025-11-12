# AI Console - Java Application with Microsoft Semantic Kernel

A Java console application that demonstrates how to use Microsoft's Semantic Kernel for Java to create an interactive AI chat experience.

## Features

- 🤖 Interactive AI chat using OpenAI or Azure OpenAI
- 🧠 Powered by Microsoft Semantic Kernel for Java (latest version 1.4.4-RC2)
- 💬 Persistent conversation history during session
- 🎯 Agent-based architecture for structured AI interactions
- 🔧 Easy configuration with environment variables

## Prerequisites

- **Java 17 or higher** (JDK required)
- **Maven 3.6+** (for building)
- **OpenAI API Key** or **Azure OpenAI Service** access

## Setup

### 1. Environment Configuration

Copy the example environment file and configure your API credentials:

```bash
cp .env.example .env
```

Edit `.env` with your API credentials:

**For OpenAI:**
```bash
CLIENT_KEY=your_openai_api_key_here
MODEL_ID=gpt-4o
```

**For Azure OpenAI:**
```bash
AZURE_CLIENT_KEY=your_azure_openai_key_here
CLIENT_ENDPOINT=https://your-resource-name.openai.azure.com/
MODEL_ID=your_deployment_name
```

### 2. Load Environment Variables

```bash
# Load environment variables (Linux/Mac)
source .env

# Or export them individually
export CLIENT_KEY=your_api_key_here
```

### 3. Build and Run

```bash
# Install dependencies and compile
./mvnw clean compile

# Run the application
./mvnw exec:java

# Or build and run as JAR
./mvnw clean package
java -jar target/ai-console-1.0.0.jar
```

## Dependencies

This project uses the latest Microsoft Semantic Kernel for Java dependencies:

- `semantickernel-api` - Core Semantic Kernel API
- `semantickernel-aiservices-openai` - OpenAI connector
- `semantickernel-agents` - Agent framework (new in 1.4.4)
- `semantickernel-plugin-core` - Plugin support

## Usage

Once running, you can:

- **Chat**: Type any message to chat with the AI
- **Clear**: Type `clear` to reset conversation history  
- **Help**: Type `help` to see available commands
- **Exit**: Type `exit`, `quit`, or `bye` to close the application

## Example Interaction

```
============================================================
Welcome to the AI Console powered by Semantic Kernel!
============================================================
Initializing Semantic Kernel...
Using OpenAI API
✅ Semantic Kernel initialized successfully!
📱 Model: gpt-4o

💬 Start chatting! Type 'exit', 'quit', or 'bye' to end the conversation.
📝 Type 'clear' to clear chat history.
ℹ️  Type 'help' for available commands.
------------------------------------------------------------
You: Hello! What is the Semantic Kernel?
AI: Hello! Semantic Kernel is Microsoft's open-source SDK that allows you to easily integrate AI services like OpenAI, Azure OpenAI, and Hugging Face into your applications...

You: Can you give me a code example?
AI: Certainly! Here's a simple example of how to use Semantic Kernel in Java...
```

## Project Structure

```
├── src/main/java/com/example/aiconsole/
│   └── AIConsole.java                 # Main application class
├── pom.xml                           # Maven dependencies
├── .env.example                      # Environment variables template
└── README.md                         # This file
```

## Key Features of the Implementation

### Agent-Based Architecture
Uses the new `ChatCompletionAgent` from Semantic Kernel 1.4.4 for structured AI interactions.

### Environment-Based Configuration
Supports both OpenAI and Azure OpenAI with automatic detection based on environment variables.

### Interactive Chat Loop
Maintains conversation context and provides a user-friendly command interface.

### Error Handling
Graceful error handling with informative messages for common configuration issues.

## Troubleshooting

### Common Issues

1. **ClassNotFoundException**: Ensure all dependencies are properly downloaded
   ```bash
   ./mvnw clean install
   ```

2. **API Key Issues**: Verify your environment variables are set correctly
   ```bash
   echo $CLIENT_KEY  # Should show your API key
   ```

3. **Build Issues**: Make sure you have Java 17+ installed
   ```bash
   java -version
   ```

## Implementation Status

✅ **Complete**: 
- Maven project structure with proper dependencies
- Microsoft Semantic Kernel 1.4.4-RC2 integration
- OpenAI and Azure OpenAI service support
- Interactive console application
- Environment-based configuration
- Error handling and validation
- Standalone JAR build with Maven Shade plugin

📦 **Built Artifacts**:
- `target/ai-console-1.0.0.jar` - Standalone executable JAR with all dependencies

🔧 **Available Commands**:
- `./run.sh` - Run with Maven and environment validation
- `./run-jar.sh` - Run standalone JAR with environment validation  
- `mvn exec:java` - Run via Maven exec plugin
- `java -jar target/ai-console-1.0.0.jar` - Run standalone JAR directly

## Next Steps

- Add custom plugins for specific functionality
- Implement memory/vector store integration
- Add support for multiple AI models
- Create web interface using Spring Boot

## Resources

- [Semantic Kernel Java Documentation](https://learn.microsoft.com/en-us/semantic-kernel/)
- [Semantic Kernel Java GitHub](https://github.com/microsoft/semantic-kernel-java)
- [OpenAI API Documentation](https://platform.openai.com/docs)
- [Azure OpenAI Service](https://azure.microsoft.com/en-us/products/ai-services/openai-service)