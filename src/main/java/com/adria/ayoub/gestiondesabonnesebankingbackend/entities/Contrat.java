package com.adria.ayoub.gestiondesabonnesebankingbackend.entities;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.enums.Statut;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Contrat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50) @NotNull
    private String intitule;

    @Column(length = 10) @NotNull
    @Enumerated(EnumType.STRING)
    private Statut statut;

    @OneToOne
    @JoinColumn(name = "abonne_id")
    private Abonne abonne;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "contrat_offre",
            joinColumns = @JoinColumn(name = "contrat_id"),
            inverseJoinColumns = @JoinColumn(name = "offre_id")
    )
    private List<Offre> offres = new ArrayList<>();

    /**
     * Pour ajouter un offre à la liste des offres
     * @param offre à ajouter
     * @return true si l'offre est ajouté, false si l'offre deja existe
     */
    public boolean ajouterOffre(Offre offre) {
        if (offres.stream().anyMatch(o -> o.getId().equals(offre.getId()))) {
            return false;
        }
        offres.add(offre);
        return true;
    }

    /**
     * Pour retirer un offre du contrat
     * @param offre objet
     */
    public void retirerOffre(Offre offre){
        offres.remove(offre);
        offre.getContrats().remove(this);
    }
}
