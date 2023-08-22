package com.adria.ayoub.gestiondesabonnesebankingbackend.controllers;

import com.adria.ayoub.gestiondesabonnesebankingbackend.services.ContratService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contrat")
public class ContratController {

    private ContratService contratService;

    public ContratController(ContratService contratService){
        this.contratService = contratService;
    }
}
