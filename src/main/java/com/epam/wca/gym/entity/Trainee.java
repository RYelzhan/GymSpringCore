package com.epam.wca.gym.entity;

import com.epam.wca.gym.service.ProfileService;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TRAINEES")
public class Trainee extends User {
    private static ProfileService profileService;

    @Column(name = "DATE_OF_BIRTH")
    private ZonedDateTime dateOfBirth;

    @Column(name = "ADDRESS")
    private String address;

    @OneToMany(mappedBy = "trainee",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    private Set<Training> trainings;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TRAINEE_TRAINER_MAPPING",
            joinColumns = @JoinColumn(name = "TRAINEE_ID"),
            inverseJoinColumns = @JoinColumn(name = "TRAINER_ID"))
    private Set<Trainer> trainersAssigned;

    public Trainee(String firstName,
                   String lastName,
                   ZonedDateTime dateOfBirth,
                   String address) {

        super(firstName,
                lastName,
                profileService.createUserName(firstName, lastName),
                profileService.createPassword(),
                true);

        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.trainings = new HashSet<>();
        this.trainersAssigned = new HashSet<>();
    }

    public static void setProfileService(ProfileService profileService) {
        Trainee.profileService = profileService;
    }

    @Override
    public String toString() {
        return super.toString() +
                "dateOfBirth = " + dateOfBirth + '\n' +
                "address = " + address + '\n';
    }
}
