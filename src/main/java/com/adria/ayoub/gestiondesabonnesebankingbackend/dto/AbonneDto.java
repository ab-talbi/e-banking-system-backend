package com.adria.ayoub.gestiondesabonnesebankingbackend.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class AbonneDto {

    @Column(length = 32)
    @Pattern(regexp = "^([a-zA-z\\s]{3,30})$", message = "Le nom doit etre valide, exemples : Nom, NOM, MON NOM...")
    private String nom;

    @Column(length = 32)
    @Pattern(regexp = "^([a-zA-z\\s]{3,30})$", message = "Le prénom doit etre valide, exemple : Prenom, PRENOM, Mon Prenom")
    private String prenom;

    @Column(length = 50)
    @Email(message = "l'émail n'est pas valide")
    private String email;

    @Column(length = 300)
    @NotNull(message = "adresse doit etre valide et ne dépasse pas 300 lettres")
    @NotBlank(message = "adresse doit etre valide et ne dépasse pas 300 lettres")
    private String adresse;

    @Column(length = 50)
    @Pattern(regexp = "(0|\\+212|00212)[5-7][0-9]{8}", message = "le numéro de télephone n'est pas valide, il doit etre comme l'un de ces exemples : 0512345678, 0612345678, 0712345678, +212512345678, +212612345678, +212712345678, 00212512345678, 00212612345678, 00212712345678")
    private String telephone;

    @Column(length = 10)
    @NotNull(message = "sexe doit etre valide")
    @NotBlank(message = "sexe doit etre valide")
    private String sexe;

    @Column(length = 10)
    @NotNull(message = "statut doit etre valide")
    @NotBlank(message = "statut doit etre valide")
    private String statut;

    private Long contratId;

    private Long agenceId;

    private Long backOfficeId;

}
