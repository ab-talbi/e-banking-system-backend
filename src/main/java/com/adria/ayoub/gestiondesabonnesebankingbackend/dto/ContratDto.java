package com.adria.ayoub.gestiondesabonnesebankingbackend.dto;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.enums.Statut;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class ContratDto {
    @Column(length = 50)
    @NotNull(message = "intitule doit etre valide et ne dépasse pas 50 lettres")
    @NotBlank(message = "intitule doit etre valide et ne dépasse pas 50 lettres")
    private String intitule;

    @Column(length = 10)
    @NotNull(message = "statut doit etre valide et contient soit ACTIF ou SUSPENDU")
    @NotBlank(message = "statut doit etre valide et contient soit ACTIF ou SUSPENDU")
    @Enumerated(EnumType.STRING)
    private Statut statut;

    private Long abonneId;

    private List<Long> offresIds = new ArrayList<>();
}
