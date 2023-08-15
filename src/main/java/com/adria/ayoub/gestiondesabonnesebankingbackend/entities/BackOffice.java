package com.adria.ayoub.gestiondesabonnesebankingbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString @Getter @Setter
public class BackOffice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50) @NotBlank
    private String nom;

    @Column(length = 50) @NotBlank
    private String prenom;

    @Column(length = 50) @NotBlank
    private String email;
}
