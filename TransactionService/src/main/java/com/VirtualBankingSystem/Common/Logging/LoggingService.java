package com.VirtualBankingSystem.Common.Logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoggingService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${vbank.logging.topic:vbank-logs-topic}")
    private String loggingTopic;

    public void logRequest(Object request) {
        sendLogMessage(request, "Request");
    }

    public void logResponse(Object response) {
        sendLogMessage(response, "Response");
    }

    private void sendLogMessage(Object content, String messageType) {
        try {
            String jsonContent = objectMapper.writeValueAsString(content);
            
            Map<String, Object> logMessage = new HashMap<>();
            logMessage.put("message", jsonContent);
            logMessage.put("messageType", messageType);
            logMessage.put("dateTime", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            
            String logMessageJson = objectMapper.writeValueAsString(logMessage);
            kafkaTemplate.send(loggingTopic, logMessageJson);
        } catch (Exception e) {
            System.err.println("Failed to send log message: " + e.getMessage());
        }
    }
}
