# credorax-payment-gateway
Credorax Test Task

To test:
- `mvn spring-boot:run` inside the checked out folder
- `mvn clean package` and `java -jar target/payment-gateway-service-0.0.1-SNAPSHOT.jar`
- Swagger UI can be found and tested on `http://localhost:8080/swagger-ui.html`

Ideas for future improvements:
- Complete writing audit message via messaging to a message broker topic
- Write functional tests in a separate submodule
- Implement DB retries for writing transactions (take use of spring-retry library)
- Implement transactions in service and handle concurrent transactions (either can handle internally, or use external tools for this, e.g. Camel or plain Kafka processing to make operations linear per user)
