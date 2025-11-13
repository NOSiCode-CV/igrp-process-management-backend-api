# iGRP Process – Delegate Configuration and Usages

## 1. Overview

In iGRP Process configuration, Service Tasks play a central role in automating actions within a workflow. These tasks can be enhanced using delegates, which are expressions that define how specific operations—such as webhook calls, JSON parsing, email sending, or message dispatching to brokers like Kafka—are executed.
Each delegate expression corresponds to a specific implementation logic. For example:
•	Webhook Delegate (${igrpWebhookDelegate}): Used to send HTTP requests to external services.
•	JSON Parser Delegate (${igrpJsonParseDelegate}): Parses raw or Base64-encoded JSON data.
•	Email Delegate (${igrpSendEmailDelegate}): Sends emails based on configured parameters.
•	Message Broker Delegate (${igrpMessageBrokerSenderDelegate}): Sends messages to Kafka or other message brokers.
These delegates allow dynamic and flexible integration with external systems, enabling data exchange, notifications, and event-driven communication within the process flow. Proper configuration of parameters and variable mapping ensures that data is correctly handled and passed between tasks.

## 2. Walkthrough

### 2.1 Scenario and Cases

This section outlines practical scenarios where delegates are used to perform specific actions:
•	Webhook Call Scenario: A service task sends a request to a "Users" endpoint. The response is stored in a variable (e.g., testeData) and specific fields are extracted using dot-notation or array indexing. These fields are then mapped into a payload for a subsequent webhook call.
•	JSON Parsing Scenario: A JSON string (either raw or Base64-encoded) is parsed using the JSON parser delegate. The result is stored in a variable (e.g., parsedData) and can be accessed directly or used in expressions for further processing.
These scenarios demonstrate how data flows between tasks and how delegates facilitate external communication and data transformation.

### 2.2 Steps

Here’s a clear breakdown of the steps involved in configuring and using delegates in service tasks:

1. Design your process

