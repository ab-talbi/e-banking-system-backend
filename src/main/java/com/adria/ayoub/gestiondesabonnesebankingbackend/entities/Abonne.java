package com.adria.ayoub.gestiondesabonnesebankingbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString @Getter @Setter
public class Abonne {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50) @NotBlank
    private String nom;

    @Column(length = 50) @NotBlank
    private String prenom;

    @Column(length = 50) @NotBlank
    private String email;

    @Column(length = 300) @NotBlank
    private String adresse;

    @Column(length = 50) @NotBlank
    private String telephone;

    @Column(length = 10) @NotBlank
    @Enumerated(EnumType.STRING)
    private Sexe sexe;

    @Column(length = 10) @NotBlank
    @Enumerated(EnumType.STRING)
    private Statut statut;

    @OneToOne
    @JoinColumn(name = "contrat_id")
    private Contrat contrat;

    @ManyToOne
    @JoinColumn(name = "agence_id")
    private Agence agence;

    @ManyToOne
    @JoinColumn(name = "backoffice_id")
    private BackOffice backOffice;
}
