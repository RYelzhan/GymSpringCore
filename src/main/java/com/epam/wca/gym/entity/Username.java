package com.epam.wca.gym.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USERNAMES")
public class Username {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "BASE_USERNAME")
    private String baseUserName;
    @Column(name = "COUNTER", nullable = false)
    private Long counter;

    public Username(String baseUserName, Long counter) {
        this.baseUserName = baseUserName;
        this.counter = counter;
    }

    @Override
    public String toString() {
        return "Username{" +
                "baseUserName='" + baseUserName + '\'' +
                ", counter=" + counter +
                '}';
    }
}
