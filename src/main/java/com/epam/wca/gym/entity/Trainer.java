package com.epam.wca.gym.entity;

import com.epam.wca.gym.service.ProfileService;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    private static ProfileService profileService;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TRAINING_TYPE_ID", nullable = false)
    private TrainingType specialization;

    @OneToMany(mappedBy = "trainer",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    private Set<Training> trainings;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "TRAINEE_TRAINER_MAPPING",
            joinColumns = @JoinColumn(name = "TRAINER_ID"),
            inverseJoinColumns = @JoinColumn(name = "TRAINEE_ID"))
    private Set<Trainee> traineesAssigned;

    public Trainer(String firstName,
                   String lastName,
                   TrainingType specialization) {
        super(firstName,
                lastName,
                profileService.createUserName(firstName, lastName),
                profileService.createPassword(),
                true);

        this.specialization = specialization;
        this.trainings = new HashSet<>();
        this.traineesAssigned = new HashSet<>();
    }

    public static void setProfileService(ProfileService profileService) {
        Trainer.profileService = profileService;
    }

    @Override
    public String toString() {
        return super.toString() +
                "specialization = " + specialization + '\n';
    }
}
