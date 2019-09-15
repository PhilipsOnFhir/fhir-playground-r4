package org.github.philipsonfhir.cdshooks.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.github.philipsonfhir.cdshooks.test.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PatientViewHookTester implements CdsServiceImplementationInterface {

    private final CdsService cdsService;

    @Autowired
    public PatientViewHookTester(CdsHooksService cdsHooksService ){
        this.cdsService = new CdsService();
        cdsService.setHook("patient-view");
        cdsService.setDescription("Test service for patient-view");
        cdsService.setName("Test patient-view");
        cdsService.setTitle(cdsService.getName());
        cdsService.setId("patient-view");
        cdsHooksService.addCdsService(this);
    }

    @Override
    public CdsService getCdsService(){
        return cdsService;
    }

    @Override
    public CdsServiceResponse getCallCdsService(CdsServiceCallBody body) {
        CdsServiceResponse cdsServiceResponse = new CdsServiceResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        Card card = new Card();
        card.setSummary("patient-view card");
        try {
            card.setDetail( objectMapper.writeValueAsString(body) );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        cdsServiceResponse.getCards().add(card);
//        cdsServiceResponse.setCards( new ArrayList<>());

        return cdsServiceResponse;
    }
}
