package com.adria.ayoub.gestiondesabonnesebankingbackend.services.impl;

import com.adria.ayoub.gestiondesabonnesebankingbackend.dto.ContratDto;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Abonne;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Contrat;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.Offre;
import com.adria.ayoub.gestiondesabonnesebankingbackend.entities.enums.Statut;
import com.adria.ayoub.gestiondesabonnesebankingbackend.exceptions.AlreadyExistsException;
import com.adria.ayoub.gestiondesabonnesebankingbackend.exceptions.AlreadyRelatedException;
import com.adria.ayoub.gestiondesabonnesebankingbackend.exceptions.NotFoundException;
import com.adria.ayoub.gestiondesabonnesebankingbackend.help.SortEtOrder;
import com.adria.ayoub.gestiondesabonnesebankingbackend.repositories.AbonneRepository;
import com.adria.ayoub.gestiondesabonnesebankingbackend.repositories.ContratRepository;
import com.adria.ayoub.gestiondesabonnesebankingbackend.repositories.OffreRepository;
import com.adria.ayoub.gestiondesabonnesebankingbackend.services.ContratService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContratServiceImpl implements ContratService {

    private final int PAGE_SIZE = 10;

    private ContratRepository contratRepository;
    private OffreRepository offreRepository;
    private AbonneRepository abonneRepository;

    public ContratServiceImpl(ContratRepository contratRepository, OffreRepository offreRepository, AbonneRepository abonneRepository){
        this.contratRepository = contratRepository;
        this.offreRepository = offreRepository;
        this.abonneRepository = abonneRepository;
    }

    /**
     * Pour trouver une list des contrats
     * @param search mot clé (intitule, statut ou abonné)
     * @param val valeur à chercher
     * @param page numero de page
     * @param sort pour filtrer (field et direction)
     * @return une page des contrats
     */
    @Override
    public Page<Contrat> trouverLesContrats(String search, String val, int page, String[] sort) {

        page = page > 0 ? page : 0;

        List<Sort.Order> orders = SortEtOrder.getOrdersFromSortParam(sort);

        Pageable pagingSort = PageRequest.of(page, PAGE_SIZE, Sort.by(orders));

        Page<Contrat> pageContrats;

        if (search == null || val == null){
            pageContrats = contratRepository.findAll(pagingSort);
        }else {
            if(search.equals("intitule")){
                pageContrats = contratRepository.findByIntituleContainingIgnoreCase(val,pagingSort);
            }else if(search.equals("statut")){
                if(val.equalsIgnoreCase("ACTIF") || val.equalsIgnoreCase("SUSPENDU")){
                    Statut statut = Statut.valueOf(val.toUpperCase());
                    pageContrats = contratRepository.findByStatut(statut,pagingSort);
                }else{
                    pageContrats = Page.empty(pagingSort);
                }
            }else if(search.equals("abonne")){
                pageContrats = contratRepository.findByNomOuPrenomAbonneContains(val,pagingSort);
            }else{
                pageContrats = Page.empty(pagingSort);
            }
        }

        return pageContrats;
    }

    /**
     * Pour trouver un contrat
     * @param id de contrat à trouver
     * @return Optional<Contrat>
     */
    @Override
    public Contrat trouverUnContratById(Long id) throws NotFoundException {
        Optional<Contrat> contratOptional = contratRepository.findById(id);
        if(contratOptional.isPresent()){
            Contrat contrat = contratOptional.get();
            return contrat;
        }else{
            throw new NotFoundException("Ce Contrat n'existe pas");
        }
    }

    /**
     * Pour ajouter un contrat
     * @param contratDto
     * @return objet de type Contrat
     */
    @Override
    public Contrat ajouterContrat(ContratDto contratDto) throws AlreadyRelatedException, NotFoundException {
        Contrat contrat;
        Abonne abonne;
        List<Offre> offres;
        Statut statut;
        if(contratDto.getAbonneId() != null){
            Optional<Abonne> abonneOptional = abonneRepository.findById(contratDto.getAbonneId());

            if(abonneOptional.isPresent()){
                if(!abonneOptional.get().alreadyRelatedToAContrat()){
                    abonne = abonneOptional.get();
                }else{
                    throw new AlreadyRelatedException("L'abonné que vous essayer d'associer à ce contrat, déja associé à une autre contrat!");
                }
            }else{
                throw new NotFoundException("L'abonné que vous essaye d'ajouter à ce contrat n'existe pas!");
            }
        }else{
            abonne = null;
        }

        if(contratDto.getOffresIds() != null){
            List<Offre> _offres = offreRepository.findAllById(contratDto.getOffresIds());
            if(!_offres.isEmpty()){
                offres = _offres;
            }else{
                offres = null;
            }
        }else{
            offres = null;
        }

        if(contratDto.getStatut().equalsIgnoreCase("ACTIF")){
            statut = Statut.ACTIF;
        }else{
            statut = Statut.SUSPENDU;
        }

        contrat = new Contrat(null, contratDto.getIntitule(), statut, abonne, offres);

        if(abonne != null){
            abonne.setContrat(contrat);
        }

        return contratRepository.save(contrat);
    }

    /**
     * Pour modifier un contrat
     * @param id du contrat
     * @param contratDto dto
     * @return un objet de type Contrat
     */
    @Override
    public Contrat modifierContrat(Long id,ContratDto contratDto) throws NotFoundException, AlreadyRelatedException {
        Optional<Contrat> contratOptional = contratRepository.findById(id);

        if (contratOptional.isPresent()) {
            Contrat contrat = contratOptional.get();
            contrat.setIntitule(contratDto.getIntitule());

            if(contratDto.getStatut().equalsIgnoreCase("ACTIF")){
                contrat.setStatut(Statut.ACTIF);
            }else{
                contrat.setStatut(Statut.SUSPENDU);
            }

            if(contratDto.getAbonneId() != null){
                Optional<Abonne> abonneOptional = abonneRepository.findById(contratDto.getAbonneId());
                if(abonneOptional.isPresent()){
                    if(!abonneOptional.get().alreadyRelatedToAContratExceptThis(id)){
                        contrat.associerAbonne(abonneOptional.get());
                    }else{
                        throw new AlreadyRelatedException("L'abonné que vous essayer d'associer à ce contrat, déja associé à une autre contrat!");
                    }
                }else{
                    throw new NotFoundException("L'abonné que vous essaye d'ajouter à ce contrat n'existe pas!");
                }
            }

            if(contratDto.getOffresIds() != null){
                List<Offre> offres = offreRepository.findAllById(contratDto.getOffresIds());
                if(!offres.isEmpty()){
                    contrat.setOffres(offres);
                }
            }

            return contratRepository.save(contrat);

        } else {
            throw new NotFoundException("Cet offre n'existe pas pour etre modifié!");
        }
    }

    /**
     * Pour supprimer un contrat
     * @param id du contrat à supprimer
     */
    @Override
    public void supprimerContratById(Long id) {
        contratRepository.deleteById(id);
    }

    /**
     * Pour supprimer tous les contrats
     */
    @Override
    public void supprimerTousLesContrats() {
        contratRepository.deleteAll();
    }

    /**
     * Pour changer le statut du cintrat
     * @param id du contrat
     * @param requestBody la valeur de statut
     * @return un objet de type Contrat
     */
    @Override
    public Contrat changerLeStatutDuContrat(Long id, String requestBody) throws NotFoundException {
        Optional<Contrat> contratOptional = contratRepository.findById(id);

        if(contratOptional.isPresent()){
            Contrat contrat = contratOptional.get();

            String statutString = requestBody.replaceAll("\"", "").trim();

            String statutUpper = statutString.toUpperCase();

            if(statutUpper.equals("ACTIF") || statutUpper.equals("SUSPENDU")){
                Statut statut = Statut.valueOf(statutUpper);
                contrat.setStatut(statut);
            }

            return contratRepository.save(contrat);
        }else{
            throw new NotFoundException("Ce Contrat n'existe pas pour changer son statut!");
        }
    }

    /**
     * Pour ajouter un offre à un contrat
     * @param id du contrat
     * @param offre_id
     * @return un objet de type contrat
     */
    @Override
    public Contrat ajouterUnOffreAUnContrat(Long id, Long offre_id) throws NotFoundException, AlreadyExistsException {
        Optional<Contrat> contratOptional = contratRepository.findById(id);
        Optional<Offre> offreOptional = offreRepository.findById(offre_id);

        if(contratOptional.isPresent()){
            if(offreOptional.isPresent()){
                Contrat contrat = contratOptional.get();
                if(contrat.ajouterOffre(offreOptional.get())){
                    return contratRepository.save(contrat);
                }else{
                    throw new AlreadyExistsException("Cet offre existe déja parmis les offres de ce contrat!");
                }
            }else{
                throw new NotFoundException("l'offre que vous voulez ajouter au contrat n'existe pas!");
            }
        }else{
            throw new NotFoundException("contrat n'existe pas!");
        }
    }

    /**
     * Pour retirer un offre d'un contrat
     * @param id du contrat
     * @param offre_id
     * @return un objet de type Contrat
     */
    @Override
    public Contrat retirerUnOffreDansUnContrat(Long id, Long offre_id) throws NotFoundException {
        Optional<Contrat> contratOptional = contratRepository.findById(id);
        Optional<Offre> offreOptional = offreRepository.findById(offre_id);

        if(contratOptional.isPresent()){
            if(offreOptional.isPresent()){
                Contrat contrat = contratOptional.get();
                contrat.retirerOffre(offreOptional.get());
                return contratRepository.save(contrat);
            }else{
                throw new NotFoundException("l'offre n'existe pas!");
            }
        }else{
            throw new NotFoundException("contrat n'existe pas!");
        }
    }

    /**
     * Pour retirer tous les offres du contrat
     * @param id du contrat
     * @return un objet de type Contrat
     */
    @Override
    public Contrat retirerTousLesOffreDansUnContrat(Long id) throws NotFoundException {
        Optional<Contrat> contratOptional = contratRepository.findById(id);

        if(contratOptional.isPresent()){
            Contrat contrat = contratOptional.get();
            contrat.retirerTousLesOffres();
            return contratRepository.save(contrat);
        }else{
            throw new NotFoundException("contrat n'existe pas!");
        }
    }
}
