package com.epam.wca.gym.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TRAINERS")
public class Trainer extends User {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TRAINING_TYPE_ID", nullable = false)
    private TrainingType specialization;

    @OneToMany(mappedBy = "trainer",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<Training> trainings;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "TRAINEE_TRAINER_MAPPING",
            joinColumns = @JoinColumn(name = "TRAINER_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "TRAINEE_ID", referencedColumnName = "ID"))
    private Set<Trainee> traineesAssigned;

    public Trainer(
            String username,
            String firstName,
            String lastName,
            TrainingType specialization
    ) {
        super(
                firstName,
                lastName,
                username
        );

        this.specialization = specialization;
        this.trainings = new HashSet<>();
        this.traineesAssigned = new HashSet<>();
    }

    @PreRemove
    public void removeTrainees() {
        for (Trainee trainee : traineesAssigned) {
            trainee.getTrainersAssigned().remove(this);
        }
    }

    @Override
    public String toString() {
        return super.toString() +
                "specialization = " + specialization + '\n';
    }
}
