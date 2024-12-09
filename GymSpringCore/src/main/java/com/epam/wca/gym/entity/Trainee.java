package com.epam.wca.gym.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TRAINEES")
public class Trainee extends User {
    @Column(name = "DATE_OF_BIRTH")
    private ZonedDateTime dateOfBirth;

    @Column(name = "ADDRESS")
    private String address;

    @OneToMany(mappedBy = "trainee",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    private Set<Training> trainings;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "TRAINEE_TRAINER_MAPPING",
            joinColumns = @JoinColumn(name = "TRAINEE_ID"),
            inverseJoinColumns = @JoinColumn(name = "TRAINER_ID"))
    private Set<Trainer> trainersAssigned;

    public Trainee(
            Long id,
               String username,
               String firstName,
               String lastName,
               ZonedDateTime dateOfBirth,
               String address
    ) {
        super(
                id,
                firstName,
                lastName,
                username,
                true
        );

        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.trainings = new HashSet<>();
        this.trainersAssigned = new HashSet<>();
    }

    @PreRemove
    public void clearJoinTableEntriesWithTrainers() {
        trainersAssigned.clear();
    }

    public void addTrainers(List<Trainer> trainerList) {
        trainersAssigned.addAll(trainerList);
    }

    @Override
    public String toString() {
        return super.toString() +
                "dateOfBirth = " + dateOfBirth + '\n' +
                "address = " + address + '\n';
    }
}
