package com.ucu.topicos.services;

import com.ucu.topicos.mapper.ProviderMapper;
import com.ucu.topicos.model.Invitation;
import com.ucu.topicos.model.ProviderEntity;
import com.ucu.topicos.repository.InvitationRepository;
import com.ucu.topicos.repository.ProviderRepository;
import dtos.Provider;
import dtos.ProviderRequest;
import dtos.ProvidersResponse;
import dtos.UserDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProviderService {

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private UserService userService;


    public ProvidersResponse getProviders (String userId, String nombre, String rut,
                                           Integer puntajeDesde, Integer puntajeHasta,
                                           Integer offset, String category) throws Exception {

        try{
            ProvidersResponse response = new ProvidersResponse();
            UserDTO userDTO = this.userService.verifyToken(userId);
            List<ProviderEntity> providers = providerRepository.findAll();

            if (userDTO != null){
                String role = userDTO.getRole();

                if (role.equalsIgnoreCase("ADMIN")){

                    List<ProviderEntity> filteredProviders = providers.stream()
                            .filter(p -> this.appliesFilter(p.getName(), nombre))
                            .filter(p -> this.appliesFilter(p.getRut(), rut))
                            .filter(p -> this.appliesFilter(p.getCategory(), category))
                            .filter(p -> this.appliesScoreFromFilter(p, puntajeDesde))
                            .filter(p -> this.appliesScoreToFilter(p, puntajeHasta))
                            .collect(Collectors.toList());

                    response.setPages(filteredProviders.isEmpty() ? 0 : filteredProviders.size() / 9);
                    response.setProviders(ProviderMapper.mapProviders(filteredProviders, offset));

                    return response;
                }
                if (role.equalsIgnoreCase("SOCIO")){
                    List<Invitation> invitations = invitationRepository.findAll();

                    List<String> providerRutPerSocio = invitations.stream()
                            .filter(p -> p.getInviter().getId().equals(userDTO.getUserId()))
                            .map(p -> p.getInvitee().getRut())
                            .collect(Collectors.toList());

                    List<ProviderEntity> filteredProviders = providers.stream()
                            .filter(p -> providerRutPerSocio.contains(p.getRut()))
                            .filter(p -> this.appliesFilter(p.getName(), nombre))
                            .filter(p -> this.appliesFilter(p.getRut(), rut))
                            .filter(p -> this.appliesFilter(p.getCategory(), category))
                            .filter(p -> this.appliesScoreFromFilter(p, puntajeDesde))
                            .filter(p -> this.appliesScoreToFilter(p, puntajeHasta))
                            .collect(Collectors.toList());

                    response.setPages(filteredProviders.isEmpty() ? 0 : filteredProviders.size() / 9);
                    response.setProviders(ProviderMapper.mapProviders(filteredProviders, offset));
                    return response;
                }
            }
            throw new Exception();

        }catch (Exception e){
            throw new Exception();
        }
    }

    private boolean appliesScoreFromFilter(ProviderEntity p, Integer score) {
        if (p.getScore() == null){
            return false;
        }
        if (score == null){
            return true;
        }
        return p.getScore() >= score;
    }

    private boolean appliesScoreToFilter(ProviderEntity p, Integer score) {
        if (p.getScore() == null){
            return false;
        }
        if (score == null){
            return true;
        }
        return p.getScore() <= score;
    }

    private boolean appliesFilter(String field, String filter) {
        if (field == null){
            return false;
        }
        if (filter == null){
            return true;
        }
        return field.toLowerCase().contains(filter.toLowerCase());
    }

    public void addProvider (ProviderRequest dto){

        ProviderEntity toSaveEntity = new ProviderEntity();
        List<ProviderEntity> providers = providerRepository.findAll();

        List<ProviderEntity> filteredProviders = providers.stream()
                .filter( p -> p.getRut().contains(dto.getProvider().getRut()))
                .collect(Collectors.toList());

        if (!filteredProviders.isEmpty()){
            toSaveEntity = filteredProviders.get(0);
        }
        toSaveEntity.setName(dto.getProvider().getName());
        toSaveEntity.setLogo(dto.getProvider().getLogo());
        toSaveEntity.setRut(dto.getProvider().getRut());
        toSaveEntity.setAddress(dto.getProvider().getAddress());
        toSaveEntity.setEmail(dto.getProvider().getEmail());
        toSaveEntity.setCategory(dto.getProvider().getCategory());

        //calculo score
        double scoreByQuestion = 100 / dto.getQuestions().size();
        int scorableQuestions = dto.getQuestions().stream().filter(q -> q.getScorable()).collect(Collectors.toList()).size();
        toSaveEntity.setScore(scoreByQuestion * scorableQuestions);

        this.providerRepository.save(toSaveEntity);
    }


    public Provider getProvider (String rut){

        try{
            Provider response = new Provider();

            List<ProviderEntity> providers = providerRepository.findAll();

            List<ProviderEntity> filteredProviders = providers.stream()
                    .filter(p -> null == rut || p.getRut().contains(rut))
                    .collect(Collectors.toList());

            if (!filteredProviders.isEmpty()){
                response.setLogo(filteredProviders.get(0).getLogo());
                response.setName(filteredProviders.get(0).getName());
                response.setRut(filteredProviders.get(0).getRut());
                response.setEmail(filteredProviders.get(0).getEmail());
                response.setAddress(filteredProviders.get(0).getAddress());
            }else {
                return null;
            }

            return response;

        }catch (Exception e){
            return null;
        }
    }
}
