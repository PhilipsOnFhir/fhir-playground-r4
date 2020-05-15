package org.github.philipsonfhir.fhirproxy.memoryserver;

import ca.uhn.fhir.context.FhirContext;
import com.google.common.collect.ArrayListMultimap;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.PathEngineException;
import org.hl7.fhir.instance.model.api.IBaseResource;

import org.hl7.fhir.r4.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.utils.FHIRPathEngine;
import org.hl7.fhir.r4.hapi.validation.PrePopulatedValidationSupport;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class SearchParameters {

    private final List<SearchParameter> searchparams;
    private final ArrayListMultimap<String, SearchParameter> multiHashtable;
    private final FHIRPathEngine fhirPathEngine;
    private final HostServices hostServices;

    public SearchParameters(){
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("memoryserver/search-parameters.json");
        FhirContext ourCtx= FhirContext.forR4();

        IBaseResource baseResource = ourCtx.newJsonParser().parseResource(is);

        Bundle bundle = (Bundle) baseResource;
        List<SearchParameter> list = this.searchparams = bundle.getEntry().stream()
                .map( entry -> entry.getResource())
                .filter( resource -> resource instanceof SearchParameter )
                .map( resource -> (SearchParameter)resource)
                .collect(Collectors.toList());

        multiHashtable = ArrayListMultimap.create();

        list.forEach( searchParameter -> {
            searchParameter.getBase().forEach(base  -> {
                multiHashtable.put( base.getCode(), searchParameter );
            });
        });

        HapiWorkerContext hapiWorkerContext = new HapiWorkerContext( ourCtx, new PrePopulatedValidationSupport() );
        this.fhirPathEngine = new FHIRPathEngine(hapiWorkerContext);
        this.hostServices = new HostServices();
        this.fhirPathEngine.setHostServices( hostServices );
    }

    public SearchParameter getSearchParam(ResourceType resourceType, String param) {
        List<SearchParameter> foundList = multiHashtable.get(resourceType.name()).stream().filter(searchParameter -> searchParameter.getCode().equals(param)).collect(Collectors.toList());
        if ( foundList.size()>0 ){
            return foundList.get(0);
        }
        return null;
    }

    public boolean checkResource( Resource resource, String param, String value ) {
        SearchParameter searchParameter = getSearchParam(resource.getResourceType(), param);
        if ( searchParameter ==null ){ return  false; }
//        fhirPathEngine.evaluate( resource, searchParameter.getExpression() );
        List<Base> resultList = fhirPathEngine.evaluate( resource, searchParameter.getExpression()+" = '"+value+"'" );
        if ( resultList.size()==0 || !(resultList.get(0) instanceof BooleanType) ){
            return false;
        }

        return ((BooleanType)resultList.get(0)).booleanValue();
    }

    public boolean checkResource(Resource resource, Map<String, String> queryParams) {
        boolean match = true;
        Iterator<Map.Entry<String, String>> it = queryParams.entrySet().iterator();
        while ( it.hasNext() && match ){
            Map.Entry<String, String> entry = it.next();
            match = checkResource( resource, entry.getKey(), entry.getValue() );
        }
        return match;
    }

    public List<SearchParameter> getSearchParam(ResourceType resourceType) {
        return  multiHashtable.get(resourceType.name()).stream().collect(Collectors.toList());
    }

    public List<SearchParameter> getSearchParam() {
        return this.searchparams;
    }

    static class HostServices implements FHIRPathEngine.IEvaluationContext {
        private Map<String, Object> paramMap = new HashMap<>();

        @Override
        public Base resolveConstant(Object appContext, String name, boolean beforeContext) throws PathEngineException {
            return null;
        }

        @Override
        public TypeDetails resolveConstantType(Object appContext, String name) throws PathEngineException {
            return null;
        }

        @Override
        public boolean log(String argument, List<Base> focus) {
            return false;
        }

        @Override
        public FunctionDetails resolveFunction(String functionName) {
            return null;
        }

        @Override
        public TypeDetails checkFunction(Object appContext, String functionName, List<TypeDetails> parameters) throws PathEngineException {
            return null;
        }

        @Override
        public List<Base> executeFunction(Object appContext, String functionName, List<List<Base>> parameters) {
            return null;
        }

        @Override
        public Base resolveReference(Object appContext, String url) throws FHIRException {
            return null;
        }

        @Override
        public boolean conformsToProfile(Object appContext, Base item, String url) throws FHIRException {
            return false;
        }

        @Override
        public ValueSet resolveValueSet(Object o, String s) {
            return null;
        }

        public void addVariable(String name, Object value ) {
            paramMap.put(name, value );
        }

    }
}
