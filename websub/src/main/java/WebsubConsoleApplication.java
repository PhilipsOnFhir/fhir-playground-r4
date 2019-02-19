import com.github.philipsonfhir.fhircast.app.FhirCastWebsubClient;
import org.hl7.fhir.dstu3.model.Patient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WebsubConsoleApplication {

    public static void main(String[] args) throws IOException {
        String topic;
        String baseUrl;
        if ( args.length<2 ){
            topic = "demo";
            baseUrl = "http://localhost:9080/fhircast/";
        } else {
            topic = args[0];
            baseUrl = args[1];
        }

        System.out.println("TopicUrl : "+baseUrl+"/"+topic+"/websub" );
        FhirCastWebsubClient fhirCastWebsubClient = new FhirCastWebsubClient( baseUrl, topic );


        boolean continueApp = true;
        while ( continueApp ){
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("-----------------------------------\n");
            System.out.print("Enter command\n");
            String s = br.readLine();
            switch ( s ){
                case "exit": continueApp=false; break;
                case "subscribe": fhirCastWebsubClient.subscribePatientChange(); break;
                case "unsubscribe": fhirCastWebsubClient.unSubscribePatientChange(); break;
                case "close": fhirCastWebsubClient.close(); break;
                case "logout": fhirCastWebsubClient.logout(); break;
                case "current" :
                    Patient patient = fhirCastWebsubClient.getCurrentPatient();
                    System.out.println("Patient id: "+(patient!=null?patient.getId():"null"));
                    break;
            }
            if ( s.startsWith( "open " )){
                String patientID = s.replace( "open ","" ).trim();
                System.out.println( "change to patient "+patientID);
                fhirCastWebsubClient.setCurrentPatient( (Patient) new Patient().setId(patientID) );
            }
        }
    }


}


