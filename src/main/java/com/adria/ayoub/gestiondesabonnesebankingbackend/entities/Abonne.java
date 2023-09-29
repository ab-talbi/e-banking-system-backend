package com.adria.ayoub.gestiondesabonnesebankingbackend.entities;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.enums.Sexe;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.enums.Statut;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Abonne {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String adresse;
    private String telephone;
    @Enumerated(EnumType.STRING)
    private Sexe sexe;
    @Enumerated(EnumType.STRING)
    private Statut statut;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contrat_id")
    private Contrat contrat;

    @ManyToOne
    @JoinColumn(name = "agence_id", referencedColumnName = "id")
    private Agence agence;

    @ManyToOne
    @JoinColumn(name = "backoffice_id", referencedColumnName = "id")
    private BackOffice backOffice;

    /**
     * Pour associer un contrat
     * @param contrat
     */
    public void associerContrat(Contrat contrat){
        //disassocier contrat si existe
        disassocierContrat();

        contrat.setAbonne(this);
        setContrat(contrat);

    }

    /**
     * Pour disassocier un contrat
     */
    public void disassocierContrat(){
        if(this.contrat!=null){
            this.contrat.setAbonne(null);
            setContrat(null);
        }
    }

    /**
     * Pour savoir est ce que l'attribut Contrat est deja rempli avec une autre contrat
     * @param contrat_id
     * @return soit true ou false
     */
    public boolean alreadyRelatedToAContratExceptThis(Long contrat_id){
        if(this.contrat!=null){
            if(this.contrat.getId() == contrat_id){
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Pour savoir est ce que l'attribut Contrat est deja rempli
     * @return true or false
     */
    public boolean alreadyRelatedToAContrat(){
        if(this.contrat!=null){
            return true;
        }
        return false;
    }
}
