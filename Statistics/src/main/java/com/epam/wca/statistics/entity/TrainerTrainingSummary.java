package com.epam.wca.statistics.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Document("statistics-practice")
@NoArgsConstructor
@AllArgsConstructor
public class TrainerTrainingSummary {
    @Id
    private String id;
    @Indexed(unique = true)
    private String username;
    @Indexed(unique = true)
    private String firstname;
    @Indexed(unique = true)
    private String lastname;
    private Boolean status;

    @JsonSerialize(using = IntegerKeyMapSerializer.class)
    @JsonDeserialize(using = IntegerKeyMapDeserializer.class)
    private Map<Integer, Map<Integer, Integer>> summary;

    public TrainerTrainingSummary(
            String username,
            String firstname,
            String lastname,
            Boolean status,
            Map<Integer, Map<Integer, Integer>> summary
    ) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.status = status;
        this.summary = summary;
    }
}
