package com.example.KafkaConsumer;

import com.example.mappers.DriverRecordMapper;
import com.example.mappers.ModelAttributesMapper;
import com.example.model.Repository.DriverRepository;
import com.example.model.Repository.AlertRepository;
import com.example.model.model.*;
import com.example.service.DriverRecordService;
import com.example.service.EmailService;
import com.example.service.PredictionService;
import com.example.service.RedisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class KafkaConsumer {

    private final PredictionService predictionService;
    private final DriverRecordService driverRecordService;
    private final ModelAttributesMapper modelAttributesMapper;
    private final DriverRecordMapper driverRecordMapper;
   private final  RedisService redisService ;
    private static final Logger logger = Logger.getLogger(KafkaConsumer.class.getName());

    @Autowired
    public KafkaConsumer(PredictionService predictionService,
                         DriverRecordService driverRecordService,
                         ModelAttributesMapper modelAttributesMapper,
                         DriverRecordMapper driverRecordMapper,
                        RedisService redisService) {
        this.predictionService = predictionService;
        this.driverRecordService = driverRecordService;
        this.modelAttributesMapper = modelAttributesMapper;
        this.driverRecordMapper = driverRecordMapper;
        this.redisService = redisService;
    }

    @KafkaListener(topics = "FuelOpt", groupId = "flask-api-group")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            Map<String, Object> data = new ObjectMapper().readValue(record.value(), Map.class);
            ModelAttributes modelAttributes = modelAttributesMapper.map(data);
            Map<String, Object> predictionResponse = predictionService.getPrediction(modelAttributes);
            DriverRecord driverRecord = driverRecordMapper.map(data, modelAttributes, predictionResponse);

            double predictedFuelConsumption = driverRecord.getPredictedFuelConsumption();
            int driverId = driverRecord.getDriverId();
            redisService.saveFuelConsumption(driverId, predictedFuelConsumption);
            driverRecordService.saveDriverRecord(driverRecord);
            logger.info("Saved to database");
        } catch (Exception e) {
            logger.info("Error processing Kafka message: " + e.getMessage());
        }
    }


}
