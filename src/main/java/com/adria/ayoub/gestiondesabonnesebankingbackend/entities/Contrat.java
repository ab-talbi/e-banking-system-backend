package com.adria.ayoub.gestiondesabonnesebankingbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString @Getter @Setter
public class Contrat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50) @NotBlank
    private String intitule;

    @Column(length = 10) @NotBlank
    @Enumerated(EnumType.STRING)
    private Statut statut;

    @OneToOne
    @JoinColumn(name = "abonne_id")
    private Abonne abonne;

    //@OneToMany
    //private List<Offre> offre;
}
