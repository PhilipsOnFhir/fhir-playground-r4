package org.github.philipsonfhir.fhirproxy.common.profile;

import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.ElementDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Slice {
    private final ElementDefinition ed;
    private List<Slice> sliceList = new  ArrayList<>();
    private Slice currentSlice = null;

    Slice(ElementDefinition ed){
        this.ed = ed;
    }
//    Slice(ElementDefinition ed, String sliceName) {
//        this.ed = ed;
//        this.name = sliceName;
//    }

    public String toString(){
        return this.ed.toString();
    }

//    public void addEds(List<ElementDefinition> edList) {
//        ListIterator<ElementDefinition> listIt = edList.listIterator();
//
//        while( listIt.hasNext() ){
//            ElementDefinition subEd = listIt.next();
//            String path = subEd.getPath().replace("[x]","");
//
//            processEd( subEd, listIt );
//

//                boolean done = false;
//                while( listIt.hasNext() && !done ){
//                    ElementDefinition elementDefinition = listIt.next();
//                    if ( !elementDefinition.getPath().startsWith(path) ){
//                        slice.addEds(sliceEd);
//                        this.sliceList.add(slice);
//                        return elementDefinition;
//
//                    } else {
//                        if ( elementDefinition.hasSliceName() && elementDefinition.getPath().equals(path) && !sliceEd.isEmpty() ) {
//                            slice.addEds(sliceEd);
//                            this.sliceList.add(slice);
//
//                            sliceEd = new ArrayList<>();
//                            slice = new Slice( ed, elementDefinition.getSliceName() );
//
//                        } else {
//                            sliceEd.add(elementDefinition);
//                        }
//                    }
//                }
//
//                for ( Slice slice: sliceLists ) {
//                    List<String> paths = slice.getValueDiscriminatorPaths();
//                    List<Base> discriminatorValues = new ArrayList<>();
//
//                    for ( Base srcEntry: srcEntries ) {
//                        boolean match = true;
//                        expressionProcessor.setBase((Base) srcEntry);
//                        for (String discPath : paths) {
//                            List<Base> srcValues = (List<Base>) expressionProcessor.evaluateFhirPath(discPath.replace("[x]", ""));
//                            for (Base srcValue : srcValues) {
//                                match = match & slice.match(discPath, srcValue);
//                            }
//                        }
//                        if (match) {
//                            System.out.println(srcEntry + " in slice ");
//                        }
//                    }
//                }
//
//                    List<ElementDefinition.ElementDefinitionSlicingDiscriminatorComponent> discriminators = sliceDefiniton.getDiscriminator();
//                if ( discriminators.get(0).getType().toCode().equals("value")) {
//                    String dPath = ed.getPath() + "." + discriminators.get(0).getPath().replace("[x]", "");
//
//                    // slicing defined
//                    // next of the same path will be first slice - name = sliceName|| null
//                    // followed by definitions of that slice
//                    // end with new path or same path (next slice)
//
//                        for ( Base )
////                        expressionProcessor.setBase(  );
//
//                        sliceLists.get(0).forEach( elementDefinition -> {
//                            if ( elementDefinition.getPath().equals(dPath)){
//                                Base refValue = inSrcValue0.get(0);
//                                Base discValue = elementDefinition.getFixed();
//                                boolean equal = refValue.equalsDeep(discValue);
//                                System.out.println(elementDefinition.getFixed()+ " " + refValue.equalsDeep(discValue));
//                            }
//                        });
//
//                    }
//                    // get current values for each slice in src
//                    // process each existing
//                    // create new ones if not present and min>present.
//                    // process new one
//                }
//            }
//        this.edList.add( elementDefinition );
//            } else {
//                Slice subSlice = new Slice(subEd);
////                this.sliceList.add( subSlice );
//        }
//
//    }

    public void addEd(ElementDefinition elementDefinition) {
        if ( currentSlice!=null ){
            String slicePath = currentSlice.getPath().replace("[x]","");
            if( elementDefinition.getPath().startsWith(slicePath)){
                currentSlice.addEd( elementDefinition);
            } else {
                currentSlice = null;
                addEd( elementDefinition );
            }
        } else if ( elementDefinition.hasSlicing() ){
            currentSlice = new Slice( elementDefinition );
            this.sliceList.add( currentSlice );
        } else {
            this.sliceList.add( new Slice( elementDefinition ) );
        }
    }

//    private void processEd(ElementDefinition subEd, ListIterator<ElementDefinition> listIt) {
//        if (  subEd.hasSlicing() ) {
//            addSlice( subEd, listIt );
//        } else {
//            this.sliceList.add( new Slice( subEd ));
//        }
//    }

//    private void addSlice(ElementDefinition sliceEd, ListIterator<ElementDefinition> listIt) {
//        Slice slice = new Slice( sliceEd, sliceEd.getSliceName() );
//        this.sliceList.add( slice );
//        String slicePath = sliceEd.getPath().replace("[x]","");
//        List<ElementDefinition> edList = new ArrayList<>();
//        boolean sliceDone = false;
//        while ( listIt.hasNext() && !sliceDone){
//            ElementDefinition ed = listIt.next();
//            if( ed.getPath().startsWith(slicePath)){
//                edList.add(ed);
//            } else {
//                sliceDone = true;
//                slice.addEds( edList );
//                processEd( ed, listIt );
//            }
//        }
//    }

    private String getPath() {
        return this.ed.getPath();
    }
//
//    private static ProfileElementDefinition getProfileElementDefinitions(ElementDefinition ed, ListIterator<ElementDefinition> it) {
//        ProfileElementDefinition profileElementDefinition = new ProfileElementDefinition(ed);
//        String path = ed.getPath();
//
//        boolean done = false;
//        while( it.hasNext() && !done ){
//            ElementDefinition elementDefinition = it.next();
//            if ( !elementDefinition.getPath().startsWith(path) ){
//                done =true;
//            } else {
//                if ( elementDefinition.hasSliceName() && elementDefinition.getPath().equals(path) && !slice.isEmpty() ) {
//                    slice = new Slice( ed.getSlicing(), elementDefinition.getSliceName() );
//                    sliceList.add(slice);
//                } else {
//                    slice.add(elementDefinition);
//                }
//            }
//        }
//        return profileElementDefinition;
//    }
    public List<String> getValueDiscriminatorPaths(){
        return  this.ed.getSlicing().getDiscriminator().stream()
                .filter( elementDefinitionSlicingDiscriminatorComponent ->
                    elementDefinitionSlicingDiscriminatorComponent.getType() == ElementDefinition.DiscriminatorType.VALUE )
                .map( elementDefinitionSlicingDiscriminatorComponent ->
                        elementDefinitionSlicingDiscriminatorComponent.getPath() )
                .collect(Collectors.toList());

    }

    public boolean match(String discPath, Base srcValue) {
        boolean match = false;
        List<ElementDefinition> edList = new ArrayList();
        for (ElementDefinition elementDefinition : edList) {
            if (elementDefinition.getPath().equals(discPath)) {
                Base discValue = elementDefinition.getFixed();
                match = srcValue.equalsDeep(discValue);
            }
        }
        return match;
    }

}
