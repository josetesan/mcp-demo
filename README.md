# Model Context Protocol (MCP) Demo

A demonstration of Spring AI's Model Context Protocol (MCP) with a travel agency application that uses AI to enhance the user experience.

## Project Overview

This project demonstrates how to build an AI-powered travel agency application using Spring AI's Model Context Protocol (MCP). It consists of three microservices:

1. **Travel Agency Service**: Core backend service that manages customers, destinations, and travel packages
2. **Amadeus Recommendations Service**: Provides travel location recommendations and shopping activities via the Amadeus API
3. **MCP Client**: Web frontend with chat interface that connects users to the AI assistant

The application uses Spring AI to create an AI assistant that helps users plan travel, suggest destinations, and provide shopping recommendations.

## Architecture

```
┌────────────┐      ┌─────────────────┐     ┌──────────────────┐
│            │      │                 │     │                  │
│ MCP Client ├──────┤ Travel Agency   ├─────┤ PostgreSQL       │
│            │      │                 │     │                  │
└─────┬──────┘      └─────────────────┘     └──────────────────┘
      │
      │             ┌─────────────────┐
      └─────────────┤ Amadeus         │
      │             │ Recommendations │
      |             └─────────────────┘
      │             ┌─────────────────┐
      └─────────────┤ Ollama         │
                    │ Running in host │
                    └─────────────────┘
```

## Prerequisites

- Docker and Docker Compose
- Amadeus API credentials (sign up at [Amadeus for Developers](https://developers.amadeus.com/))
- Anthropic API key (if using Claude as the LLM)

## Setup Instructions

1. Clone this repository:

   ```
   git clone <repository-url>
   cd mcp-demo
   ```

2. Create an `.env` file in the `mcp-amadeus` directory with your Amadeus API credentials:

   ```
   AMADEUS_API_KEY=your_amadeus_api_key
   AMADEUS_API_SECRET=your_amadeus_api_secret
   ```

3. If using Anthropic Claude, add your API key to the environment:

   ```
   export ANTHROPIC_API_KEY=your_anthropic_api_key
   ```

4. Start the application using Docker Compose:
   ```
   docker-compose up -d
   ```

## Usage

Once the application is running:

1. Access the web interface at http://localhost:9000/chat
2. Start chatting with the AI assistant about travel plans
3. The assistant can help with:
   - Finding destinations based on country and budget
   - Creating a customer profile
   - Booking travel packages
   - Providing shopping activity recommendations for destinations

## Service Endpoints

### Travel Agency Service (http://localhost:8060)

- Swagger UI: http://localhost:8060/swagger-ui.html
- API Documentation: http://localhost:8060/api-docs

Key endpoints:

- `/api/customers` - Customer management
- `/api/destinations` - Destination information
- `/api/travel-packages` - Travel package booking

### Amadeus Recommendations (http://localhost:8080)

- `/api/recommended-locations` - Get recommended travel locations
- `/api/shopping-activities` - Get shopping activities for locations

## Components in Detail

### Travel Agency Service

The core service that manages:

- Customer information
- Destination data (cities, countries, prices)
- Travel package bookings

### Amadeus Recommendations Service

Integrates with the Amadeus API to provide:

- Travel destination recommendations
- Shopping and activity recommendations for destinations

### MCP Client

A web-based chat interface that:

- Connects users to the AI assistant
- Provides a conversational interface for travel planning
- Uses Spring AI's Model Context Protocol to coordinate between services

## Development

To build and run individual services for development:

1. Travel Agency Service:

   ```
   cd travel-agency
   ./mvnw spring-boot:run -Dspring.profiles.active=local
   ```

2. Amadeus Recommendations:

   ```
   cd mcp-amadeus
   ./mvnw spring-boot:run
   ```

3. MCP Client:
   ```
   cd mcp-client
   ./mvnw spring-boot:run
   ```

## License

This project is open source and available under the [MIT License](LICENSE).
