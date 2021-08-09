# credorax-payment-gateway
Credorax Test Task


Ideas for future improvements:
- Complete writing audit message via messaging to a message broker topic
- Write functional tests in a separate submodule
- Implement DB retries for writing transactions (take use of spring-retry library)
- Implement transactions in service and handle concurrent transactions (either can handle internally, or use external tools for this, e.g. Camel or plain Kafka processing to make operations linear per user)
