package com.adria.ayoub.gestiondesabonnesebankingbackend.services.impl;

import com.adria.ayoub.gestiondesabonnesebankingbackend.dto.AbonneDto;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.*;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.enums.Sexe;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.enums.Statut;
import com.adria.ayoub.gestiondesabonnesebankingbackend.exceptions.AlreadyRelatedException;
import com.adria.ayoub.gestiondesabonnesebankingbackend.exceptions.NotFoundException;
import com.adria.ayoub.gestiondesabonnesebankingbackend.help.SortEtOrder;
import com.adria.ayoub.gestiondesabonnesebankingbackend.repositories.AbonneRepository;
import com.adria.ayoub.gestiondesabonnesebankingbackend.repositories.AgenceRepository;
import com.adria.ayoub.gestiondesabonnesebankingbackend.repositories.BackOfficeRepository;
import com.adria.ayoub.gestiondesabonnesebankingbackend.repositories.ContratRepository;
import com.adria.ayoub.gestiondesabonnesebankingbackend.services.AbonneService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AbonneServiceImpl implements AbonneService {

    private final int PAGE_SIZE = 10;

    private AbonneRepository abonneRepository;
    private AgenceRepository agenceRepository;
    private BackOfficeRepository backOfficeRepository;
    private ContratRepository contratRepository;

    public AbonneServiceImpl(AbonneRepository abonneRepository, AgenceRepository agenceRepository, BackOfficeRepository backOfficeRepository, ContratRepository contratRepository){
        this.abonneRepository = abonneRepository;
        this.agenceRepository = agenceRepository;
        this.backOfficeRepository = backOfficeRepository;
        this.contratRepository = contratRepository;
    }

    /**
     * Pour trouver une list des abonnes à partir de clé
     * @param search nom, prenom, tel, email, statut, sexe...
     * @param val clé à chercher
     * @param page numero
     * @param sort pour filtrer (field,direction)
     * @return une page des abonnes
     */
    @Override
    public Page<Abonne> trouverLesAbonnes(String search, String val, int page, String[] sort) {

        page = page > 0 ? page : 0;

        List<Sort.Order> orders = SortEtOrder.getOrdersFromSortParam(sort);

        Pageable pagingSort = PageRequest.of(page, PAGE_SIZE, Sort.by(orders));

        Page<Abonne> pageAbonnes;

        if (search == null || val == null){
            pageAbonnes = abonneRepository.findAll(pagingSort);
        }else {
            if(search.equals("nom")){
                pageAbonnes = abonneRepository.findByNomContainingIgnoreCase(val,pagingSort);
            }else if(search.equals("prenom")){
                pageAbonnes = abonneRepository.findByPrenomContainingIgnoreCase(val,pagingSort);
            }else if(search.equals("email")){
                pageAbonnes = abonneRepository.findByEmailContainingIgnoreCase(val,pagingSort);
            }else if(search.equals("adresse")){
                pageAbonnes = abonneRepository.findByAdresseContainingIgnoreCase(val,pagingSort);
            }else if(search.equals("telephone")){
                pageAbonnes = abonneRepository.findByTelephoneContainingIgnoreCase(val,pagingSort);
            }else if(search.equals("statut")){
                if(val.equalsIgnoreCase("ACTIF") || val.equalsIgnoreCase("SUSPENDU")){
                    Statut statut = Statut.valueOf(val.toUpperCase());
                    pageAbonnes = abonneRepository.findByStatut(statut,pagingSort);
                }else{
                    pageAbonnes = Page.empty(pagingSort);
                }
            }else if(search.equals("sexe")){
                if(val.equalsIgnoreCase("HOMME") || val.equalsIgnoreCase("FEMME")){
                    Sexe sexe = Sexe.valueOf(val.toUpperCase());
                    pageAbonnes = abonneRepository.findBySexe(sexe,pagingSort);
                }else{
                    pageAbonnes = Page.empty(pagingSort);
                }
            }else if(search.equals("contrat")){
                pageAbonnes = abonneRepository.findByIntituleContratContains(val,pagingSort);
            }else if(search.equals("agence")){
                Long id = stringToLong(val);
                if(id != null){
                    pageAbonnes = abonneRepository.findByAgence(id,pagingSort);
                }else{
                    pageAbonnes = Page.empty(pagingSort);
                }
            }else if(search.equals("backoffice")){
                Long id = stringToLong(val);
                if(id != null){
                    pageAbonnes = abonneRepository.findByBackOffice(id,pagingSort);
                }else{
                    pageAbonnes = Page.empty(pagingSort);
                }
            }else{
                pageAbonnes = Page.empty(pagingSort);
            }
        }
        return pageAbonnes;
    }

    /**
     * Pour trouver un abonné avec l'id passé au parametre
     * @param id de l'abonné
     * @return Abonne
     */
    @Override
    public Abonne trouverUnAbonneById(Long id) throws NotFoundException {
        Optional<Abonne> abonneOptional = abonneRepository.findById(id);
        if(abonneOptional.isPresent()){
            Abonne abonne = abonneOptional.get();
            return abonne;
        }else{
            throw new NotFoundException("Cet Abonne n'existe pas");
        }
    }

    /**
     * Pour ajouter un abonné
     * @param abonneDto
     * @return un objet de type abonné
     */
    @Override
    public Abonne ajouterAbonne(AbonneDto abonneDto) throws NotFoundException, AlreadyRelatedException {
        Abonne abonne;

        Contrat contrat;
        Agence agence;
        BackOffice backOffice;

        Statut statut = abonneDto.getStatut().equalsIgnoreCase("ACTIF") ? Statut.ACTIF : Statut.SUSPENDU;
        Sexe sexe = abonneDto.getSexe().equalsIgnoreCase("FEMME") ? Sexe.FEMME : Sexe.HOMME;

        if(abonneDto.getContratId() != null){
            Optional<Contrat> contratOptional = contratRepository.findById(abonneDto.getContratId());

            if(contratOptional.isPresent()){
                if(!contratOptional.get().alreadyRelatedToAnAbonne()){
                    contrat = contratOptional.get();
                }else{
                    throw new AlreadyRelatedException("Le contrat que vous essayer d'associer à cet abonné, déja associé à un autre abonné!");
                }
            }else{
                throw new NotFoundException("Le contrat que vous essaye d'ajouter à cet abonné n'existe pas!");
            }
        }else{
            contrat = null;
        }

        if(abonneDto.getAgenceId() != null){
            Optional<Agence> agenceOptional = agenceRepository.findById(abonneDto.getAgenceId());

            if(agenceOptional.isPresent()){
                agence = agenceOptional.get();
            }else{
                throw new NotFoundException("L'agence que vous essaye d'associer à cet abonné n'existe pas!");
            }
        }else{
            agence = null;
        }

        if(abonneDto.getBackOfficeId() != null){
            Optional<BackOffice> backOfficeOptional = backOfficeRepository.findById(abonneDto.getBackOfficeId());

            if(backOfficeOptional.isPresent()){
                backOffice = backOfficeOptional.get();
            }else{
                throw new NotFoundException("Le backoffice que vous essaye d'associer à cet abonné n'existe pas!");
            }
        }else{
            backOffice = null;
        }

        abonne = new Abonne(null,
                abonneDto.getNom(),
                abonneDto.getPrenom(),
                abonneDto.getEmail(),
                abonneDto.getAdresse(),
                abonneDto.getTelephone(),
                sexe,
                statut,
                contrat,
                agence,
                backOffice);

        if(contrat != null){
            contrat.setAbonne(abonne);
        }

        return abonneRepository.save(abonne);
    }

    /**
     * Pour modifier un abonné
     * @param id de l'abonné
     * @param abonneDto dto
     * @return un objet de type Abonne
     */
    @Override
    public Abonne modifierAbonne(Long id, AbonneDto abonneDto) throws NotFoundException, AlreadyRelatedException {
        Optional<Abonne> abonneOptional = abonneRepository.findById(id);

        if (abonneOptional.isPresent()) {

            Abonne abonne = abonneOptional.get();

            abonne.setNom(abonneDto.getNom());
            abonne.setPrenom(abonneDto.getPrenom());
            abonne.setEmail(abonneDto.getEmail());
            abonne.setAdresse(abonneDto.getAdresse());
            abonne.setTelephone(abonneDto.getTelephone());
            abonne.setStatut(abonneDto.getStatut().equalsIgnoreCase("ACTIF") ? Statut.ACTIF : Statut.SUSPENDU);
            abonne.setSexe(abonneDto.getSexe().equalsIgnoreCase("FEMME") ? Sexe.FEMME : Sexe.HOMME);

            if(abonneDto.getContratId() != null){
                Optional<Contrat> contratOptional = contratRepository.findById(abonneDto.getContratId());
                if(contratOptional.isPresent()){
                    if(!contratOptional.get().alreadyRelatedToAnAbonneExceptThis(id)){
                        abonne.associerContrat(contratOptional.get());
                    }else{
                        throw new AlreadyRelatedException("Le contrat que vous essayer d'associer à cet abonné, déja associé à un autre abonné!");
                    }
                }else{
                    throw new NotFoundException("Le contrat que vous essaye d'ajouter à cet abonné n'existe pas!");
                }
            }

            if(abonneDto.getAgenceId() != null){
                Optional<Agence> agenceOptional = agenceRepository.findById(abonneDto.getAgenceId());

                if(agenceOptional.isPresent()){
                    abonne.setAgence(agenceOptional.get());
                }else{
                    throw new NotFoundException("L'agence que vous essaye d'associer à cet abonné n'existe pas!");
                }
            }

            if(abonneDto.getBackOfficeId() != null){
                Optional<BackOffice> backOfficeOptional = backOfficeRepository.findById(abonneDto.getBackOfficeId());

                if(backOfficeOptional.isPresent()){
                    abonne.setBackOffice(backOfficeOptional.get());
                }else{
                    throw new NotFoundException("Le backoffice que vous essaye d'associer à cet abonné n'existe pas!");
                }
            }

            return abonneRepository.save(abonne);

        } else {
            throw new NotFoundException("Cet abonné n'existe pas pour etre modifié!");
        }
    }

    /**
     * Pour supprimer l'abonné de cette id
     * @param id de l'abonné à supprimer
     */
    @Override
    public void supprimerAbonneById(Long id) {
        abonneRepository.deleteById(id);
    }

    /**
     * Pour supprimer tous les abonnés
     */
    @Override
    public void supprimerTousLesAbonnes() {
        abonneRepository.deleteAll();
    }

    /**
     * Pour changer le statut de l'abonné
     * @param id de l'abonné
     * @param requestBody la valeur de statut
     * @return un objet de type Abonne
     */
    @Override
    public Abonne changerLeStatutDeLAbonne(Long id, String requestBody) throws NotFoundException {
        Optional<Abonne> abonneOptional = abonneRepository.findById(id);

        if(abonneOptional.isPresent()){
            Abonne abonne = abonneOptional.get();

            String statutString = requestBody.replaceAll("\"", "").trim();

            String statutUpper = statutString.toUpperCase();

            if(statutUpper.equals("ACTIF") || statutUpper.equals("SUSPENDU")){
                Statut statut = Statut.valueOf(statutUpper);
                abonne.setStatut(statut);
            }

            return abonneRepository.save(abonne);
        }else{
            throw new NotFoundException("Cet abonné n'existe pas pour changer son statut!");
        }
    }

    /**
     * Pour associer une agence à un abonné
     * @param abonne_id de l'abonné
     * @param agence_id de l'agence
     * @return un Abonne
     */
    @Override
    public Abonne associerAgence(Long abonne_id, Long agence_id) throws NotFoundException {
        Optional<Abonne> abonneOptional = abonneRepository.findById(abonne_id);
        Optional<Agence> agenceOptional = agenceRepository.findById(agence_id);

        if(abonneOptional.isPresent()){
            if(agenceOptional.isPresent()){
                Abonne abonne = abonneOptional.get();
                Agence agence = agenceOptional.get();

                abonne.setAgence(agence);

                return abonneRepository.save(abonne);
            }else{
                throw new NotFoundException("L'agence n'existe pas!");
            }
        }else{
            throw new NotFoundException("L'abonné n'existe pas!");
        }
    }

    /**
     * Pour disassocier une agence d'un abonné
     * @param abonne_id de l'abonné
     * @return un Abonne
     */
    @Override
    public Abonne disassocierAgence(Long abonne_id) throws NotFoundException {
        Optional<Abonne> abonneOptional = abonneRepository.findById(abonne_id);

        if(abonneOptional.isPresent()){
            Abonne abonne = abonneOptional.get();
            abonne.setAgence(null);

            return abonneRepository.save(abonne);
        }else{
            throw new NotFoundException("L'abonné n'existe pas!");
        }
    }

    /**
     * Pour associer un backoffice à un abonné
     * @param abonne_id de l'abonné
     * @param backoffice_id du backoffice
     * @return un Abonne
     */
    @Override
    public Abonne associerBackOffice(Long abonne_id, Long backoffice_id) throws NotFoundException {
        Optional<Abonne> abonneOptional = abonneRepository.findById(abonne_id);
        Optional<BackOffice> backOfficeOptional = backOfficeRepository.findById(backoffice_id);

        if(abonneOptional.isPresent()){
            if(backOfficeOptional.isPresent()){
                Abonne abonne = abonneOptional.get();
                BackOffice backOffice = backOfficeOptional.get();

                abonne.setBackOffice(backOffice);

                return abonneRepository.save(abonne);
            }else{
                throw new NotFoundException("Le backoffice n'existe pas!");
            }
        }else{
            throw new NotFoundException("L'abonné n'existe pas!");
        }
    }

    /**
     * Pour disassocier un backoffice d'un abonné
     * @param abonne_id de l'abonné
     * @return un Abonne
     */
    @Override
    public Abonne disassocierBackOffice(Long abonne_id) throws NotFoundException {
        Optional<Abonne> abonneOptional = abonneRepository.findById(abonne_id);

        if(abonneOptional.isPresent()){
            Abonne abonne = abonneOptional.get();
            abonne.setBackOffice(null);

            return abonneRepository.save(abonne);
        }else{
            throw new NotFoundException("L'abonné n'existe pas!");
        }
    }

    /**
     * Pour associer un contrat à un abonné
     * @param abonne_id de l'abonné
     * @param contrat_id du contrat
     * @return un Abonne
     */
    @Override
    public Abonne associerContrat(Long abonne_id, Long contrat_id) throws NotFoundException, AlreadyRelatedException {
        Optional<Abonne> abonneOptional = abonneRepository.findById(abonne_id);
        Optional<Contrat> contratOptional = contratRepository.findById(contrat_id);

        if(abonneOptional.isPresent()){
            if(contratOptional.isPresent()){
                Abonne abonne = abonneOptional.get();
                Contrat contrat = contratOptional.get();

                if(!contrat.alreadyRelatedToAnAbonneExceptThis(abonne_id)){
                    abonne.associerContrat(contrat);
                }else{
                    throw new AlreadyRelatedException("Le contrat que vous essayer d'associer à cet abonné, déja associé à un autre abonné!");
                }

                return abonneRepository.save(abonne);
            }else{
                throw new NotFoundException("Le contrat que vous voulez ajouter à cet abonné n'existe pas!");
            }
        }else{
            throw new NotFoundException("L'abonné n'existe pas!");
        }
    }

    /**
     * Pour disassocier un contrat d'un abonné
     * @param abonne_id de l'abonné
     * @return un Abonne
     */
    @Override
    public Abonne disassocierContrat(Long abonne_id) throws NotFoundException {
        Optional<Abonne> abonneOptional = abonneRepository.findById(abonne_id);

        if(abonneOptional.isPresent()){
            Abonne abonne = abonneOptional.get();

            abonne.disassocierContrat();

            return abonneRepository.save(abonne);
        }else{
            throw new NotFoundException("L'abonné n'existe pas!");
        }
    }

    /**
     * Pour convertir un String à un Long pour les deux methodes de l'agence et backoffice
     * @param input string
     * @return Long
     */
    @Override
    public Long stringToLong(String input) {
        if (input.matches("\\d+")) { // si contient des numeriques
            try {
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }

        return null;
    }
}
