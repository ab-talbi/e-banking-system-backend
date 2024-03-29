package com.adria.ayoub.gestiondesabonnesebankingbackend.entities;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.enums.Statut;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Contrat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String intitule;

    @Enumerated(EnumType.STRING)
    private Statut statut;

    @JsonIgnore
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
        if(this.offres!=null){
            if (offres.stream().anyMatch(o -> o.getId().equals(offre.getId()))) {
                return false;
            }
        }
        offres.add(offre);
        return true;
    }

    /**
     * Pour retirer un offre du contrat
     * @param offre objet
     */
    public void retirerOffre(Offre offre){
        if(this.offres != null){
            offres.remove(offre);
            offre.getContrats().remove(this);
        }
    }

    /**
     * Pour retirer tous les offres du contrat
     */
    public void retirerTousLesOffres(){
        if(this.offres != null){
            offres.clear();
        }
    }

    /**
     * Pour associer un abonné
     * @param abonne
     */
    public void associerAbonne(Abonne abonne){
        //disassocier abonne si existe
        disassocierAbonne();

        abonne.setContrat(this);
        setAbonne(abonne);

    }

    /**
     * Pour disassocier un abonne
     */
    public void disassocierAbonne(){
        if(this.abonne!=null){
            this.abonne.setContrat(null);
            setAbonne(null);
        }
    }

    /**
     * Pour savoir est ce que l'attribut Abonne est deja rempli avec un autre abonne
     * @param abonne_id
     * @return soit true ou false
     */
    public boolean alreadyRelatedToAnAbonneExceptThis(Long abonne_id){
        if(this.abonne!=null){
            if(this.abonne.getId() == abonne_id){
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Pour savoir est ce que l'attribut Abonne est deja rempli
     * @return true or false
     */
    public boolean alreadyRelatedToAnAbonne(){
        if(this.abonne!=null){
            return true;
        }
        return false;
    }
}
