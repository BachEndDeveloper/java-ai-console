package com.example.aiconsole;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import com.example.plugins.LightPlugin;
import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.credential.KeyCredential;

import java.util.Scanner;

/**
 * AI Console Application using Microsoft Semantic Kernel for Java
 * 
 * This application demonstrates how to:
 * - Set up Semantic Kernel with OpenAI/Azure OpenAI
 * - Create a chat completion agent
 * - Handle interactive conversations
 * 
 * Required Environment Variables:
 * - For OpenAI: CLIENT_KEY (your OpenAI API key)
 * - For Azure OpenAI: AZURE_CLIENT_KEY, CLIENT_ENDPOINT
 * - Optional: MODEL_ID (defaults to gpt-4o)
 */
public class AIConsole {
    
    // Environment variables for API configuration
    private static final String CLIENT_KEY = System.getenv("CLIENT_KEY");
    private static final String AZURE_CLIENT_KEY = System.getenv("AZURE_CLIENT_KEY");
    private static final String CLIENT_ENDPOINT = System.getenv("CLIENT_ENDPOINT");
    private static final String MODEL_ID = System.getenv().getOrDefault("MODEL_ID", "gpt-4o");
    
    private static Kernel kernel;
    private static ChatCompletionService chatCompletionService;
    private static ChatHistory chatHistory;
    
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("Welcome to the AI Console powered by Semantic Kernel!");
        System.out.println("=".repeat(60));
        
        try {
            initializeSemanticKernel();
            runInteractiveChat();
        } catch (Exception e) {
            System.err.println("Error initializing AI Console: " + e.getMessage());
            System.err.println("\nPlease ensure you have set the required environment variables:");
            System.err.println("- For OpenAI: CLIENT_KEY");
            System.err.println("- For Azure OpenAI: AZURE_CLIENT_KEY and CLIENT_ENDPOINT");
            e.printStackTrace();
        }
    }
    
    /**
     * Initialize Semantic Kernel with OpenAI or Azure OpenAI
     */
    private static void initializeSemanticKernel() {
        System.out.println("Initializing Semantic Kernel...");
        
        // Create OpenAI client based on available credentials
        OpenAIAsyncClient client;
        if (AZURE_CLIENT_KEY != null && !AZURE_CLIENT_KEY.isEmpty()) {
            System.out.println("Using Azure OpenAI Service");
            client = new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(AZURE_CLIENT_KEY))
                .endpoint(CLIENT_ENDPOINT)
                .buildAsyncClient();
        } else if (CLIENT_KEY != null && !CLIENT_KEY.isEmpty()) {
            System.out.println("Using OpenAI API");
            client = new OpenAIClientBuilder()
                .credential(new KeyCredential(CLIENT_KEY))
                .buildAsyncClient();
        } else {
            throw new IllegalStateException("No API credentials found. Please set CLIENT_KEY or AZURE_CLIENT_KEY environment variable.");
        }
        
        // Create chat completion service
        chatCompletionService = OpenAIChatCompletion.builder()
            .withModelId(MODEL_ID)
            .withOpenAIAsyncClient(client)
            .build();
        
        // Create and register the LightPlugin
        System.out.println("🔌 Registering LightPlugin...");
        KernelPlugin lightPlugin = KernelPluginFactory.createFromObject(new LightPlugin(), "LightPlugin");
        
        // Build the kernel with the plugin and chat completion service
        kernel = Kernel.builder()
            .withAIService(ChatCompletionService.class, chatCompletionService)
            .withPlugin(lightPlugin)
            .build();
        
        // Initialize chat history with system message that mentions the light control capabilities
        chatHistory = new ChatHistory("You are a helpful AI assistant with smart home capabilities. " +
                "You can control lights in different locations using the available functions. " +
                "When users ask about lighting, use the appropriate functions to help them.");
        
        System.out.println("✅ Semantic Kernel initialized successfully!");
        System.out.println("📱 Model: " + MODEL_ID);
        System.out.println("💡 LightPlugin registered - I can control smart home lights!");
        System.out.println();
    }
    
    /**
     * Run interactive chat loop
     */
    private static void runInteractiveChat() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("💬 Start chatting! Type 'exit', 'quit', or 'bye' to end the conversation.");
        System.out.println("📝 Type 'clear' to clear chat history.");
        System.out.println("ℹ️  Type 'help' for available commands.");
        System.out.println("-".repeat(60));
        
        while (true) {
            System.out.print("You: ");
            String userInput = scanner.nextLine().trim();
            
            if (userInput.isEmpty()) {
                continue;
            }
            
            // Handle special commands
            if (userInput.equalsIgnoreCase("exit") || 
                userInput.equalsIgnoreCase("quit") || 
                userInput.equalsIgnoreCase("bye")) {
                System.out.println("\n👋 Thanks for chatting! Goodbye!");
                break;
            }
            
            if (userInput.equalsIgnoreCase("clear")) {
                chatHistory = new ChatHistory("You are a helpful AI assistant. Provide clear, concise, and accurate responses.");
                System.out.println("🧹 Chat history cleared!");
                continue;
            }
            
            if (userInput.equalsIgnoreCase("help")) {
                showHelp();
                continue;
            }
            
            // Process user input and get AI response
            try {
                System.out.print("AI: ");
                processUserMessage(userInput);
                System.out.println();
            } catch (Exception e) {
                System.err.println("❌ Error processing message: " + e.getMessage());
            }
        }
        
        scanner.close();
    }
    
    /**
     * Process user message and get AI response with auto function calling
     */
    private static void processUserMessage(String userMessage) {
        try {
            // Add user message to history
            chatHistory.addUserMessage(userMessage);
            
            // Create invocation context with auto function calling enabled
            InvocationContext invocationContext = InvocationContext.builder()
                .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true))
                .build();
            
            // Get response from chat completion service with function calling enabled
            System.out.print("🤔 Processing");
            var response = chatCompletionService.getChatMessageContentsAsync(
                chatHistory, 
                kernel, 
                invocationContext
            ).block();
            
            System.out.print("\r"); // Clear the processing indicator
            
            if (response != null && !response.isEmpty()) {
                // Get the latest response message
                ChatMessageContent message = response.get(response.size() - 1);
                String responseText = message.getContent();
                
                if (responseText != null && !responseText.trim().isEmpty()) {
                    System.out.println(responseText);
                    
                    // Add assistant response to history
                    chatHistory.addAssistantMessage(responseText);
                } else {
                    System.out.println("I processed your request (function call completed).");
                }
            } else {
                System.out.println("I'm sorry, I couldn't generate a response.");
            }
            
        } catch (Exception e) {
            System.err.println("Error getting AI response: " + e.getMessage());
            e.printStackTrace(); // For debugging
        }
    }
    
    /**
     * Show available commands
     */
    private static void showHelp() {
        System.out.println("\n📋 Available Commands:");
        System.out.println("  exit, quit, bye - Exit the application");
        System.out.println("  clear          - Clear chat history");
        System.out.println("  help           - Show this help message");
        System.out.println("\n💡 Tips:");
        System.out.println("  - Ask questions, request explanations, or have a conversation");
        System.out.println("  - The AI remembers the conversation context");
        System.out.println("  - Use 'clear' to start fresh if needed");
        System.out.println("\n🏠 Smart Home Features:");
        System.out.println("  - Ask me to turn lights on/off in any room");
        System.out.println("  - Example: 'Turn on the lights in the living room'");
        System.out.println("  - Example: 'Turn off the bedroom lights'");
        System.out.println("  - I'll automatically call the appropriate functions!");
        System.out.println();
    }
}