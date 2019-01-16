//package com.github.philipsonfhir.fhircast.app;
//
//import org.springframework.shell.core.CommandMarker;
//import org.springframework.shell.core.annotation.CliCommand;
//import org.springframework.shell.core.annotation.CliOption;
//import org.springframework.stereotype.Component;
//
//import java.io.PrintWriter;
//
//@Component
//public class CommandLIne implements CommandMarker {
//
//    private final FhirCastWebsubClient fhirCastClient;
//
//    CommandLIne(FhirCastWebsubClient fhirCastClient ){
//        this.fhirCastClient = fhirCastClient;
//    }
//
//    @CliCommand(value = { "subscribepatient", "wg" })
//    public String register(){
//        fhirCastClient.subscribePatientChange();
//        return "OK";
//    }
////
////    @CliCommand(value = { "web-save", "ws" })
////    public String webSave(
////        @CliOption(key = "url") String url,
////        @CliOption(key = { "out", "file" }) String file) {
////        String contents = getContentsOfUrlAsString(url);
////        try ( PrintWriter out = new PrintWriter(file)) {
////            out.write(contents);
////        }
////        return "Done.";
////    }
//}