package com.adria.ayoub.gestiondesabonnesebankingbackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class BackOffice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50) @NotNull
    private String nom;

    @Column(length = 50) @NotNull
    private String prenom;

    @Column(length = 50) @NotNull
    private String email;

    @JsonIgnore
    @OneToMany(mappedBy = "backOffice",fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Abonne> abonnes = new ArrayList<>();
}
