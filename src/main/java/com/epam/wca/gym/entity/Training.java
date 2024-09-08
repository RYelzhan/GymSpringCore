package com.epam.wca.gym.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TRAININGS")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRAINEE_ID", nullable = false)
    private Trainee trainee;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRAINER_ID", nullable = false)
    private Trainer trainer;
    @Column(name = "NAME", nullable = false)
    private String trainingName;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TRAINING_TYPE_ID", nullable = false)
    private TrainingType trainingType;
    @Column(name = "DATE", nullable = false)
    private LocalDate trainingDate;
    @Column(name = "DURATION", nullable = false)
    private int trainingDuration;

    public Training(Trainee trainee,
                    Trainer trainer,
                    String trainingName,
                    TrainingType trainingType,
                    LocalDate trainingDate,
                    int trainingDuration) {
        this.trainee = trainee;
        this.trainer = trainer;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }

    @Override
    public String toString() {
        return "id = " + id + '\n' +
                "traineeId = " + trainee.getId() + '\n' +
                "trainerId = " + trainer.getId() +'\n' +
                "trainingName = " + trainingName + '\n' +
                "trainingType = " + trainingType + '\n' +
                "trainingDate = " + trainingDate + '\n' +
                "trainingDuration = " + trainingDuration + '\n';
    }
}