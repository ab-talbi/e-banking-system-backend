package com.adria.ayoub.gestiondesabonnesebankingbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString @Getter @Setter
public class Agence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50) @NotBlank
    private String nom;

    @Column(length = 300) @NotBlank
    private String adresse;
}
