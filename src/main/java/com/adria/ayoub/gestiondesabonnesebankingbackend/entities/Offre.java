package com.adria.ayoub.gestiondesabonnesebankingbackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Offre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String libelle;
    private String description;

    @JsonIgnore
    @ManyToMany(mappedBy = "offres")
    private List<Contrat> contrats = new ArrayList<>();
}
