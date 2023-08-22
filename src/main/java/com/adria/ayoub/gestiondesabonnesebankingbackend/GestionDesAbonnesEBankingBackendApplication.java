package com.adria.ayoub.gestiondesabonnesebankingbackend;

import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Agence;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.BackOffice;
import com.adria.ayoub.gestiondesabonnesebankingbackend.repositories.AgenceRepository;
import com.adria.ayoub.gestiondesabonnesebankingbackend.repositories.BackOfficeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GestionDesAbonnesEBankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionDesAbonnesEBankingBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(AgenceRepository agenceRepository, BackOfficeRepository backOfficeRepository){
        return args -> {
            agenceRepository.save(new Agence(null,"MARRAKECH ABDELKRIM EL KHATABI","N 45 IMMEUBLE AL KHATTABI AVENUE ABDELKRIM AL KHATTABI GUELIZ, Marrakech"));
            agenceRepository.save(new Agence(null,"AIN CHOCK","812 ROUTE DE MEDIOUNA, Casablanca"));
            agenceRepository.save(new Agence(null,"AGADIR DRARGA","HAY SIDI SAID DRARGA, Agadir"));

            agenceRepository.findAll().forEach(agence -> {
                System.out.println(agence.toString());
            });

            backOfficeRepository.save(new BackOffice(null,"El Amrani","Amine","amine.elamrani@example.ma"));
            backOfficeRepository.save(new BackOffice(null,"Moussaoui","Leila","leila.moussaoui@example.ma"));
            backOfficeRepository.save(new BackOffice(null,"Benjelloun","Youssef","youssef.benjelloun@example.ma"));

            backOfficeRepository.findAll().forEach(backoffice -> {
                System.out.println(backoffice.toString());
            });
        };
    }

}
