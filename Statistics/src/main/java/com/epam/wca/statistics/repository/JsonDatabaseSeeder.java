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

import static com.epam.wca.statistics.util.StatisticsConstants.COLLECTION_DATA_LOCATION;
import static com.epam.wca.statistics.util.StatisticsConstants.COLLECTION_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonDatabaseSeeder {
    private final MongoTemplate mongoTemplate;
    private final ObjectMapper mapper;

    @PostConstruct
    public void seedDatabase() throws IOException {
        List<TrainerTrainingSummary> summaries = mapper.readValue(
                new ClassPathResource(COLLECTION_DATA_LOCATION).getInputStream(),
                new TypeReference<>() {}
        );

        mongoTemplate.dropCollection(COLLECTION_NAME);
        mongoTemplate.insert(summaries, COLLECTION_NAME);

        log.info("Inserted data to database: {}\n", Arrays.toString(summaries.toArray()));
    }
}
