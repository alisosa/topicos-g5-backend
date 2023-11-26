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


    public ProvidersResponse getProviders (String inviterId, String nombre, String rut,
                                           Integer puntajeDesde, Integer puntajeHasta,
                                           Integer offset, String category) throws Exception {

        try{
            ProvidersResponse response = new ProvidersResponse();
            UserDTO userDTO = this.userService.verifyToken(inviterId);
            List<ProviderEntity> providers = providerRepository.findAll();

            if (userDTO != null){
                String role = userDTO.getRole();

                if (role.equalsIgnoreCase("Admin")){

                    List<ProviderEntity> filteredProviders = providers.stream()
                            .filter(p -> null == nombre.toLowerCase() || p.getName().contains(nombre.toLowerCase()))
                            .filter(p -> null == rut || p.getRut().contains(rut))
                            .filter(p -> null == puntajeDesde || p.getScore() >= puntajeDesde)
                            .filter(p -> null == puntajeHasta || p.getScore() <= puntajeHasta)
                            .filter(p -> null == category || p.getCategory().equalsIgnoreCase(category))
                            .collect(Collectors.toList());

                    response.setPages(filteredProviders.isEmpty() ? 0 : filteredProviders.size() / 9);
                    response.setProviders(ProviderMapper.mapProviders(filteredProviders, offset));

                    return response;
                }
                if (role.equalsIgnoreCase("Socio")){
                    List<Invitation> invitations = invitationRepository.findAll();

                    List<String> providerRutPerSocio = invitations.stream()
                            .filter(p -> p.getInviter().equals(userDTO.getUserId()))
                            .map(p -> p.getInvitee().getRut())
                            .collect(Collectors.toList());

                    List<ProviderEntity> filteredProviders = providers.stream()
                            .filter(p -> providerRutPerSocio.contains(p.getRut()))
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
