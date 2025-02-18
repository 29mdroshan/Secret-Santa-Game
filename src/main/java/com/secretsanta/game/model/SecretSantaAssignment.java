package com.secretsanta.game.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecretSantaAssignment {
    private Employee employee;
    private Employee secretChild;
}
