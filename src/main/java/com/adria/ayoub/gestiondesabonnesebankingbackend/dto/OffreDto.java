package com.adria.ayoub.gestiondesabonnesebankingbackend.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class OffreDto {

    @Column(length = 50)
    @NotNull(message = "libelle doit etre valide et ne dépasse pas 50 lettres")
    @NotBlank(message = "libelle doit etre valide et ne dépasse pas 50 lettres")
    private String libelle;

    @Column(length = 300)
    @NotNull(message = "description doit etre valide et ne dépasse pas 300 lettres")
    @NotBlank(message = "description doit etre valide et ne dépasse pas 300 lettres")
    private String description;

}