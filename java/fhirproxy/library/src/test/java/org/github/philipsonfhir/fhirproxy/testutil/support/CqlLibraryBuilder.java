package org.github.philipsonfhir.fhirproxy.testutil.support;

import org.github.philipsonfhir.fhirproxy.testutil.BaseConstants;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.codesystems.LibraryType;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class CqlLibraryBuilder {
    private final Library library;
    String id = "-";
    String version = "0.1";

    public CqlLibraryBuilder( String cqlString ) throws UnsupportedEncodingException {

        String[] words = cqlString.split("\\s");
        List<String> libraryNames = new ArrayList();
        List<String> libraryVersions = new ArrayList();
        boolean done = false;

        for ( int i=0; i< words.length && !done ; i++ ){
            String word = words[i];
            if ( word.equals("library")){
                id = words[i+1];
            }
            if ( word.equals("version")){
                version = words[i+1];
            }
            if ( word.equals("include")){
                libraryNames.add( words[i+1] );
                if ( words[i+2].equals("version")){
                    libraryVersions.add(words[i+2]);
                } else {
                    libraryVersions.add("");
                }
            }
            if ( word.equals("define")){
                done = true;
            }
        }



        library = (Library) new Library()
            .setVersion( version )
            .setStatus( Enumerations.PublicationStatus.ACTIVE )
            .setExperimental( true )
            .setUrl( getCannonical() )
            .setType(new CodeableConcept()
                    .addCoding(
                            new Coding(
                                    LibraryType.LOGICLIBRARY.getSystem(),
                                    LibraryType.LOGICLIBRARY.toCode(),
                                    LibraryType.LOGICLIBRARY.getDisplay()
                            )
                )
            )
            .addIdentifier( new Identifier()
                    .setValue( id )
                    .setUse( Identifier.IdentifierUse.OFFICIAL )
            )
            .setId(id);

        buildCqlContent( cqlString );

//        for ( int i = 0 ; i< libraryNames.size(); i++ ){
//            this.library
//                    .addRelatedArtifact( new RelatedArtifact()
//                            .setType( RelatedArtifact.RelatedArtifactType.DEPENDSON )
//                            .setResource( "http://hl7.org/Library/FHIRv400/FHIRHelpers_v400_Library.json" )
//                    )
//            ;
//
//        }
    }

    public String getId() {
        return id;
    }

    public String getCannonical() {
        return  BaseConstants.NAMESPACEURL+ ResourceType.Library.name()+"/"+getId();
    }

    public Library build(){ return library; }

    public void buildCqlContent(String cqlString) throws UnsupportedEncodingException {
        Attachment attachment = new Attachment();
        attachment.setContentType("text/cql");
        byte[] cqlData =cqlString.getBytes("utf-8");
        attachment.setData(cqlData);
        library.addContent(attachment);
    }
}
