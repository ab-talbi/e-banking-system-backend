package com.adria.ayoub.gestiondesabonnesebankingbackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Offre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50) @NotNull
    private String libelle;

    @Column(length = 300) @NotNull
    private String description;

    @JsonIgnore
    @ManyToMany(mappedBy = "offres")
    private List<Contrat> contrats = new ArrayList<>();
}
