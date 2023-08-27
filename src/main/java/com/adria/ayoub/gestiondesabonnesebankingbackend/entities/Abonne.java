package com.adria.ayoub.gestiondesabonnesebankingbackend.entities;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.enums.Sexe;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.enums.Statut;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Abonne {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50) @NotNull
    private String nom;

    @Column(length = 50) @NotNull
    private String prenom;

    @Column(length = 50) @NotNull
    private String email;

    @Column(length = 300) @NotNull
    private String adresse;

    @Column(length = 50) @NotNull
    private String telephone;

    @Column(length = 10) @NotNull
    @Enumerated(EnumType.STRING)
    private Sexe sexe;

    @Column(length = 10) @NotNull
    @Enumerated(EnumType.STRING)
    private Statut statut;

    @OneToOne
    @JoinColumn(name = "contrat_id")
    private Contrat contrat;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "agence_id", referencedColumnName = "id")
    private Agence agence;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "backoffice_id", referencedColumnName = "id")
    private BackOffice backOffice;
}
