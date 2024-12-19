package com.epam.wca.statistics.repository;

import com.epam.wca.statistics.entity.TrainerTrainingSummary;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonDatabaseSeeder {
    private final MongoTemplate mongoTemplate;

    @PostConstruct
    public void seedDatabase() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<TrainerTrainingSummary> summaries = mapper.readValue(
                new ClassPathResource("data/data.json").getInputStream(),
                new TypeReference<>() {
                }
        );

        mongoTemplate.dropCollection("statistics-practice");
        mongoTemplate.insert(summaries, "statistics-practice");

        log.info("Inserted data to database: {}\n", Arrays.toString(summaries.toArray()));

        log.info("Sample data from users.json inserted.");
    }
}
