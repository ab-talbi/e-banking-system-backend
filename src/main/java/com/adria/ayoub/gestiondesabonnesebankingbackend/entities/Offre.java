package com.adria.ayoub.gestiondesabonnesebankingbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString @Getter @Setter
public class Offre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50) @NotBlank
    private String libelle;

    @Column(length = 300) @NotBlank
    private String description;
}
