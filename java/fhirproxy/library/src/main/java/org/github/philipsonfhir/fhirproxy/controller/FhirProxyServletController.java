package org.github.philipsonfhir.fhirproxy.controller;//package org.github.philipsonfhir.smartonfhir.basic.org.github.philipsonfhir.fhirproxy.controller;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.github.philipsonfhir.fhirproxy.common.fhircall.FhirCall;
import org.github.philipsonfhir.fhirproxy.common.fhircall.FhirCallFactory;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;

@CrossOrigin(origins = "*")
@WebServlet(urlPatterns = FhirProxyServletController.base+"/*")
public class FhirProxyServletController extends HttpServlet {

    public static final String base="/fhirservlet";
    private final ServletHelper servletHelper;

    @Autowired
    FhirProxyServletController(@Value("${proxy.fhirserver.url}") String fhirServerUrl ){
        servletHelper = new ServletHelper( fhirServerUrl );
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        servletHelper.doGet(req,resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        servletHelper.doPost(req,resp);
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) {
        servletHelper.doPut(req,resp);
    }

}
