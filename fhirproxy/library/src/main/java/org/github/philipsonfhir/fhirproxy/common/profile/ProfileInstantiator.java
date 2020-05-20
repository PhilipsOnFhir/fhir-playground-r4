package org.github.philipsonfhir.fhirproxy.common.profile;


import ca.uhn.fhir.context.FhirContext;
import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.github.philipsonfhir.fhirproxy.common.FhirValueSetter;
import org.github.philipsonfhir.fhirproxy.common.expression.ExpressionProcessor;
import org.github.philipsonfhir.fhirproxy.common.fhirserver.IFhirServer;
import org.hl7.fhir.r4.model.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileInstantiator {
    public static Base instantiateProfile(StructureDefinition structureDefinition ) throws ClassNotFoundException, FhirProxyException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Base resource;// sliced elements

        ElemDefHolder elemDefHolder = getElemDefHolder(structureDefinition);

        //////
        String xml = "";
        xml = elemDefHolder.populateResource( xml );

        resource = (Base) FhirContext.forR4().newXmlParser().parseResource(xml);

        System.out.println(xml);

        if ( resource instanceof Resource ){
            if (((Resource) resource).hasMeta()){
                boolean opt = ((Resource) resource).getMeta().getProfile().stream()
                        .anyMatch(profile -> profile.equals(structureDefinition.getUrl()));
                if ( !opt ){
                    ((Resource) resource).setMeta( new Meta().addProfile(structureDefinition.getUrl()));
                }
            } else {
                ((Resource) resource).setMeta( new Meta().addProfile(structureDefinition.getUrl()));
            }
        }
        return resource;
    }

    private static ProfileInstantiator.ElemDefHolder getElemDefHolder(StructureDefinition structureDefinition) {
        ElemDefHolder elemDefHolder = null;

        for ( ElementDefinition elementDefinition: structureDefinition.getSnapshot().getElement()){
//            System.out.println( elementDefinition.getPath()+ " - " + elementDefinition.getSliceName() + "- " + elementDefinition.getMin() );

            if ( elemDefHolder==null ){
                elemDefHolder = new ElemDefHolder(elementDefinition);
            } else {
                String path = elementDefinition.getPath();
//                System.out.println(path);
                elemDefHolder.addElement(elementDefinition);
            }
        }
        return elemDefHolder;
    }

    public static Base updateBasedOnProfile2(IFhirServer fhirServer, Resource resource, StructureDefinition structureDefinition) throws FhirProxyException {
        ElemDefHolder elemDefHolder = getElemDefHolder(structureDefinition);
        ExpressionProcessor expressionProcessor = new ExpressionProcessor(fhirServer);
        expressionProcessor.setBase(resource);
        elemDefHolder.updateResource( expressionProcessor, resource );
        return resource;
    }


    static class ElemDefHolder{
        private final ElementDefinition baseElDef;
        String path = "";
        HashMap<String, ElementDefinition.ElementDefinitionSlicingComponent> sliceDefinitionMap  = new HashMap<>();
        HashMap<String, HashMap<String,ElemDefHolder>> map  = new HashMap<>();
        private String currentSlice ="";
        private ElementDefinition.ElementDefinitionSlicingComponent slicing=null;

        ElemDefHolder( ElementDefinition elementDefinition){
            this.baseElDef = elementDefinition;
            this.path = elementDefinition.getPath();
        }

        public ElemDefHolder(ElementDefinition elementDefinition, ElementDefinition.ElementDefinitionSlicingComponent slicing) {
            this( elementDefinition );
            this.slicing=slicing;
        }

        public void addElement(ElementDefinition elementDefinition) {
            HashMap<String,ElemDefHolder> sliceMap = map.get(currentSlice);
            if (sliceMap==null){
                sliceMap = new HashMap<>();
                map.put(currentSlice,sliceMap);
            }

            if (elementDefinition.getPath().startsWith(path)){
                String subPath = elementDefinition.getPath().substring(path.length()+1);
                if (( subPath.contains("."))){
                    // add to other element
                    String subSubPath = subPath.substring(0, subPath.indexOf("."));
                    ElemDefHolder sub = sliceMap.get( subSubPath );
                    sub.addElement( elementDefinition );

                } else {
                    this.currentSlice = ( elementDefinition.hasSliceName()? elementDefinition.getSliceName() : "" );
                    sliceMap = map.get(currentSlice);
                    if (sliceMap==null){
                        sliceMap = new HashMap<>();
                        map.put(currentSlice,sliceMap);
                    }
                    if ( elementDefinition.hasSlicing() ){
                        this.sliceDefinitionMap.put(subPath,elementDefinition.getSlicing());
                        sliceMap.put( subPath, new ElemDefHolder( elementDefinition,elementDefinition.getSlicing() ));
                    } else {
                        sliceMap.put( subPath, new ElemDefHolder( elementDefinition  ));
                    }

                }
            }

        }

        public String populateResource(String xml ){
            String xmlTmp = "";

            if ( path.contains(".")){
                // not root
                if  ( this.baseElDef.getMin()>0 ){
//                    System.out.println("mandatory "+path+" "+ this.baseElDef.getType().get(0).getCode());

                    String basePath = ( path.contains(".") ? path.substring( path.lastIndexOf(".")+1) : "" );
//                    Base subBase = base.makeProperty( basePath.hashCode(), basePath );
                    xmlTmp += "<"+basePath;

                    if ( this.baseElDef.hasFixed() ){
//                        System.out.println( " value "+this.baseElDef.getFixed().toString());
                        xmlTmp += " value=\""+this.baseElDef.getFixed().primitiveValue()+"\"";
                    }
                    xmlTmp += ">\n";
                    for (HashMap<String, ElemDefHolder> edh1 : this.map.values()) {
                        for (ElemDefHolder edh2 : edh1.values()) {
                            xmlTmp = edh2.populateResource(xmlTmp);
                        }
                    }
                    xmlTmp += "</"+basePath+">\n";

                }
            } else {
                xmlTmp += "<" + path+">\n";
                for (HashMap<String, ElemDefHolder> edh1 : this.map.values()) {
                    for (ElemDefHolder edh2 : edh1.values()) {
                        xmlTmp = edh2.populateResource(xmlTmp);
                    }
                }
                xmlTmp += "</" + path+">\n";
            }

            xmlTmp = (xmlTmp.contains("value") ? xml+xmlTmp : xml );

            return xmlTmp;
        }

        public Base checkResource(Base base) throws FhirProxyException {
//            this.path;
            if ( this.path.contains(".")) {
                String basePath = this.path.substring(this.path.indexOf(".")+1);
                // check fixed
                if ( this.baseElDef.hasFixed() ){
                    FhirValueSetter.setProperty( base, basePath, this.baseElDef.getFixed() );
                }
                if ( this.baseElDef.getMax().equals("0")){
                    FhirValueSetter.setProperty( base, basePath, null );
                }
            }
            for ( Map.Entry<String, HashMap<String,ElemDefHolder>> sliceEntry: this.map.entrySet() ) {
                // check slices
                System.out.println( "Slice: "+ sliceEntry.getKey() );
                for (ElemDefHolder edh2 : sliceEntry.getValue().values()) {
                    edh2.checkResource( base );
                }
            }
            return  base;
        }

        public void updateResource(ExpressionProcessor expressionProcessor, Base base) throws FhirProxyException {
            expressionProcessor.setBase(base);

//            if ( srcValues.size()< this.baseElDef.getMin()){
//                System.out.println(String.format("add %d instances of %s", this.baseElDef.getMin()-srcValues.size(), path));
//            }
            for (HashMap<String, ElemDefHolder> edh1 : this.map.values()) {
                for (ElemDefHolder edh2 : edh1.values()) {
                    String slicePath = edh2.path.substring( edh2.path.lastIndexOf(".")+1).replace("[x]","");
                    expressionProcessor.setBase(base);
                    List<Base> srcValuesOrg = (List<Base>) expressionProcessor.evaluateFhirPath(slicePath);
                    ElementDefinition.ElementDefinitionSlicingComponent relevantSliceDefinition = this.sliceDefinitionMap.get(slicePath);
                    // filter for match
                    // relevantSlice.getDiscriminator().forEach( disc-> disc.getPath() ) --> path to disc value
                    List<Base> srcValues = new ArrayList<>();
                    if ( relevantSliceDefinition!=null ){
                        for ( Base srcValue: srcValuesOrg ){
                            boolean match = true;

                            for ( ElementDefinition.ElementDefinitionSlicingDiscriminatorComponent disc: relevantSliceDefinition.getDiscriminator()){
                                String discPath = disc.getPath();
                                List<ElementDefinition> eds= edh2.getElementDefinition( discPath );
                                for( ElementDefinition edDiscValue: eds ){
                                    if ( edDiscValue.hasFixed() ) {
                                        Base fixedValue = edDiscValue.getFixed();
                                        expressionProcessor.setBase(srcValue);
                                        List<Base> discValues = (List<Base>) expressionProcessor.evaluateFhirPath(discPath);
                                        for (Base discValue : discValues) {
                                            if ( !discValue.equalsDeep( fixedValue)){
                                                match = false;
                                            }
                                        }
                                    }
                                }

                            }
                            if ( match ){
                                srcValues.add( srcValue );
                            }
                        }
//                        srcValues = srcValuesOrg;
                    } else {
                        srcValues = srcValuesOrg;
                    }

                    int nMissing = edh2.baseElDef.getMin()- srcValues.size();
                    for ( int i=0; i<nMissing; i++ ){
                        Base fixedValue = edh2.baseElDef.getFixed();
                        int size = edh2.map.size();
                        int index = srcValues.size()+i;
                        String newPath = ( index==0? slicePath : slicePath+"["+index+"]");
                        if ( edh2.baseElDef.hasFixed() ) {
                            FhirValueSetter.setProperty(base, newPath, edh2.baseElDef.getFixed());
                        } else if ( edh2.baseElDef.hasDefaultValue() ) {
                            FhirValueSetter.setProperty(base, newPath, edh2.baseElDef.getDefaultValue() );
                        }
                    }

                    for ( Base srcValue: srcValues ) {
                        if ( edh2.baseElDef.hasFixed() ){
                            Base fixedValue = edh2.baseElDef.getFixed();
                            FhirValueSetter.setProperty( base, slicePath, fixedValue );
                        }
                        edh2.updateResource(expressionProcessor, srcValue);
                    }
                }
            }
        }

        private ArrayList<ElementDefinition> getElementDefinition(String discPath) {
            ArrayList<ElementDefinition> result = new ArrayList<>();
            if ( discPath.contains(".") ){
                String startString = discPath.substring(0, discPath.indexOf("."));
                String remainder   = discPath.substring(discPath.indexOf(".")+1);
                for ( HashMap<String, ElemDefHolder> edMap : map.values() ){
                    if ( edMap.containsKey(startString)){
                        result.addAll( edMap.get(startString).getElementDefinition(remainder));
                    }
                }
            } else {
                for ( HashMap<String, ElemDefHolder> edMap : map.values() ){
                    if ( edMap.containsKey(discPath)){
                        result.add( edMap.get(discPath).baseElDef );
                    }
                }
            }
            return result;
        }
    }

}
