package org.github.philipsonfhir.fhirproxy.common.expression;

import com.google.common.io.Resources;
import org.fhir.ucum.UcumEssenceService;
import org.github.philipsonfhir.fhirproxy.common.FhirProxyError;
import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.github.philipsonfhir.fhirproxy.common.expression.baseList.BaseList;
import org.github.philipsonfhir.fhirproxy.common.fhirserver.IFhirServer;
import org.github.philipsonfhir.fhirproxy.common.util.ExtensionUtil;
import org.github.philipsonfhir.fhirproxy.common.util.ReferenceUtil;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.PathEngineException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.hapi.ctx.MyHapiWorkerContext;
import org.hl7.fhir.r4.hapi.validation.PrePopulatedValidationSupport;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.utils.FHIRPathEngine;
import org.hl7.fhir.r4.utils.MyFHIRPathEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressionProcessor {
    private final IFhirServer fhirServer;
    private final MyFHIRPathEngine fhirPathEngine;
    private final HostServices hostServices;
    private final FHIRPathEngineEvaluationContext fHIRPathEngineEvaluationContext;
//    private CqlExecutionProvider cqlExecutionProvider = null;
    private Base base;
    private IBaseResource defaultLibrary;
    private Map<String, Object > context = new HashMap<>();
    private String patientId = null;
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public ExpressionProcessor(IFhirServer fhirServer ) throws FhirProxyException {
        MyHapiWorkerContext hapiWorkerContext = new MyHapiWorkerContext( fhirServer.getCtx(), new PrePopulatedValidationSupport() );

        try{
            UcumEssenceService ucumEssenceService = new UcumEssenceService( Resources.getResource("ucum-essence.xml").openStream() );
            hapiWorkerContext.setUcumService( ucumEssenceService );
        } catch ( Exception e ){
            throw new FhirProxyException( e );
        }

        this.fhirPathEngine = new MyFHIRPathEngine(hapiWorkerContext);
        this.hostServices = new HostServices();
        this.fhirPathEngine.setHostServices( hostServices );
        this.fhirServer = fhirServer;
        this.fHIRPathEngineEvaluationContext = new FHIRPathEngineEvaluationContext();
        fhirPathEngine.setHostServices( fHIRPathEngineEvaluationContext );

    }

    public ExpressionProcessor(IFhirServer fhirServer, DomainResource base, String subjectId ) throws FhirProxyException {
        this ( fhirServer );
//        this.cqlExecutionProvider = new CqlExecutionProvider( base, patientId, fhirServer );
        setBase( base );
        this.patientId = subjectId;
    }

    public ExpressionProcessor() throws FhirProxyException {
        this( null );
    }

    /**
     * Set base resource used for FhirPath expressions.
     * @param base
     */
    public void setBase( Base base ){
        this.base = base;
    }

    public Object evaluate(Expression expression)  {
        Object result = null;
        switch( expression.getLanguage()) {
            case "application/x-fhir-query":
                String query = resolveQuery( expression.getExpression() );
                if ( fhirServer==null){
                    throw new FhirProxyError("FhirServer not specified");
                }

                if ( !query.isEmpty()) {
                    result = this.fhirServer.doGet(query);
                }
                break;
            case "text/fhirpath":
                result = fhirPathEngine.evaluate( base, expression.getExpression() );
                break;
            case "text/cql":
//                result = cqlExecutionProvider.evaluateInContext( expression.getExpression(), context );
                break;
            default:
        }
        logger.info(expression.getExpression()+" : "+result);
        if ( expression.hasName() && result!=null ){
            this.updateContext( expression.getName(), result);
            logger.info( expression.getName()+ " : "+result);
        }
        return result;
    }

    private String resolveQuery(String expression) {
        if ( expression.contains("{{")){
            String fhirPathElement = expression.substring(expression.indexOf("{{"), expression.indexOf("}}")+2);
            String fhirPathExpression = fhirPathElement.substring(2, fhirPathElement.length()-2);
            List<Base> result = this.fhirPathEngine.evaluate(base, fhirPathExpression);
            if ( result.size()>0 ) {
                String str = "";
                Base b = result.get(0);
                if ( b instanceof IdType ){
                    IdType idt = (IdType)b;
                    str = ReferenceUtil.getReference(idt);
                }
                else if ( b instanceof Reference ) {
                    str = ((Reference) result.get(0)).getReference();
                } else {
                        str = result.get(0).primitiveValue();

                }
//                    if (result instanceof Object) {
//                    Object o = (Object) result;
//                }

                expression = expression.replace(fhirPathElement, str);
            } else {
                expression = "" ;
            }
            return resolveQuery(expression);
        }
        return expression;
    }

    public void setDefaultLibrary(CanonicalType canonical) throws FhirProxyException {
        Map<String,String> queryParam = new HashMap<>();
        queryParam.put( "url", canonical.getValue() );

        if ( fhirServer==null){
            throw new FhirProxyException("FhirServer not specified");
        }
        Bundle bundle = (Bundle) fhirServer.doGet( ResourceType.Library, queryParam );
        if ( bundle.hasEntry() && bundle.getEntry().size() != 0 ){
            this.defaultLibrary = bundle.getEntry().get(0).getResource();
        }
    }

    public void updateContext(String name, Base value) {
        this.context.put( name, value );
        this.fHIRPathEngineEvaluationContext.update( name, value );
    }

    public void updateContext(String name, Object object) {
        if ( object instanceof List ) {
            List<Base> values = (List<Base>) object;
            if (values.size() == 1) {
                updateContext(name, values.get(0));
            } else {
                updateContext( name, new BaseList(values) );
            }
        } else {
            updateContext(name, (Base)object );
        }
    }

    public Object handleListResult(Object result) {
        if ( result instanceof List && ((List)result).size()==1 ){
            result = ((List)result).get(0);
        }
        return result;
    }

    public Object evaluateFhirPath(String expression) throws FhirProxyException {
        return  this.evaluate( new Expression().setLanguage("text/fhirpath").setExpression(expression));
    }

    public void updateVariables( List<Extension> extensions ) {
        List<Extension> variableExtensions = ExtensionUtil.getExtensions(extensions, ExtensionUtil.QUESTIONNAIRE_VARIABLE_EXTENSION);
        for (Extension variableExtension : variableExtensions) {
            evaluate((Expression) variableExtension.getValue());
        }
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
