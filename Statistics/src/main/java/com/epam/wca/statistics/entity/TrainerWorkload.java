package com.epam.wca.statistics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TRAINER_WORKLOAD")
@AllArgsConstructor
public class TrainerWorkload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "USERNAME", nullable = false)
    private String username;
    @Column(name = "FIRSTNAME", nullable = false)
    private String firstname;
    @Column(name = "LASTNAME", nullable = false)
    private String lastname;
    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;
    @Column(name = "TRAINING_YEAR", nullable = false)
    private Integer year;
    @Column(name = "TRAINING_MONTH", nullable = false)
    private Integer month;
    @Column(name = "DURATION", nullable = false)
    private Integer duration;

    public TrainerWorkload(
            String username,
            String firstname,
            String lastname,
            Boolean isActive,
            Integer year,
            Integer month,
            Integer duration) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.isActive = isActive;
        this.year = year;
        this.month = month;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "TrainerWorkload{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", isActive=" + isActive +
                ", year=" + year +
                ", month=" + month +
                ", duration=" + duration +
                '}';
    }
}
