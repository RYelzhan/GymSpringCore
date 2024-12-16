package com.epam.wca.authentication.entity;

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
@AllArgsConstructor
@Entity
@Table(name = "USERNAMES")
public class Username {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "BASE_USERNAME", unique = true)
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
