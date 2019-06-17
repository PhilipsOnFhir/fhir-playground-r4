import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.ImagingStudy;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.Test;

public class InjectFhirResources {
    String[] groentes = {"aardaker", "aardamandel", "knolcyperus", "aardappel", "aardpeer", "topinamboer", "abc-kruid", "champagneblad", "adelaarsvaren", "Afrikaanse baobab", "alfalfa", "luzerne", "alocasie", "reuzentaro", "amsoi", "andijvie", "antruwa", "arrowroot", "artisjok", "asperge", "asperge-erwt", "aubergine", "augurk", "ayote", "B", "bamboescheut", "bamboespruit", "bieslook", "biet", "bindsla", "bleekselderij", "bloemkool", "boomkalebas", "boon", "boerenkool", "bosui", "brandnetel", "brave hendrik", "broccoli", "broodvrucht", "bimi", "C", "cassave", "maniok", "cayennepeper", "chayote", "chilipeper", "chinese kool", "citroengras", "courgette", "D", "dok kae", "Sesbania grandiflora", "doperwt", "erwt", "duizendkoppige kool", "duizendkop of duizendknop", "E", "eeuwig moes", "duizendkoppige kool", "erwt", "eikenbladsla", "F", "fleskalebas", "flespompoen", "G", "groene kool", "savooiekool", "groenlof", "H", "haverwortel", "paarse morgenster", "boksbaard", "hertshoornweegbree", "I", "ijsbergsla", "ijskruid", "ingerolde palmvaren", "J", "jackfruit", "nangka", "K", "kailan", "kannibaaltomaat", "kappertje", "kapucijner", "kardoen", "kervel", "kikkererwt", "keker", "garbanzo", "kiwano", "kliswortel", "klit", "knolcapucien", "knolkervel", "knolribzaad", "knoflook", "knolraap", "meiknolletje", "knolselderij", "knolvenkel", "venkel", "komkommer", "konjak", "kool", "koolraap", "koolrabi", "kousenband", "kudzu", "L", "lamsoor", "zulte", "zeeaster", "lente-ui", "bosui", "linzen", "lotuswortel", "heilige lotus", "M", "mais", "meiraap", "knolraap", "molsla", "paardenbloem", "melindjoe", "mergkool", "mungboon", "muskaatpompoen", "N", "nierboon", "Nieuw-Zeelandse spinazie", "O", "oca", "okra", "olifantenyam", "olijf", "P", "paksoi", "palmkool (cavolo nero", "zwarte kool)", "paprika", "pastinaak", "patisson", "peen", "pereskia", "peterselie", "peultjes", "pijlkruid", "pijpajuin", "pompoen", "postelein", "prei", "pronkboon", "Q", "R", "raapstelen", "bladmoes", "rabarber", "radijs", "rammenas", "ook wel rettich genoemd", "repelsteeltje", "rode biet", "kroot", "rodekool", "romanesco", "roodlof", "rucola", "raketsla", "S", "sago", "savooiekool", "schorseneren", "selderij", "selderie", "sint-jansuitjes", "sjalotten", "sla", "snijbiet", "snijboon", "snijselderij", "spekboon", "sopropo", "paré", "sperzieboon", "spinazie", "spinaziezuring", "spitskool (wittekool of savooiekool)", "splijtkool", "duizendkoppige kool", "sponskomkommer", "nenwa", "spruitkool", "suikerbiet", "suikermais", "suikerwortel", "sojaboon", "T", "takako", "taro", "taugé", "tayer", "tetragonia (Nieuw-Zeelandse spinazie)", "teunisbloem", "tindola", "papasan", "tomaat", "topinamboer", "aardpeer", "tuinkers", "tuinboon", "tuinmelde", "U", "ui", "V", "veldsla", "veldzuring", "venkel", "vijgenbladpompoen", "vleugelkomkommer", "vleugelboon", "W", "waspompoen", "witte waterkers", "waterspinazie", "winterpostelein", "witlof", "wittekool", "witte mimosa", "wortel", "wortelpeterselie", "X", "Y", "yacón", "yam", "yamboon", "Z", "zeekool", "zeekraal", "zoete aardappel", "bataat", "zonnewortel", "zuring", "zuurkool", "zwarte kool (cavolo nero", "palmkool"};

    @Test
    public void injectTestResources(){

        String[][] patients = {
                {"Brocolli","Aardpeer"},
                {"Wortel","Andijvie"},
                {"Asperges","Ginger"},
                {"Witlof", "Sago"}
        };
        FhirContext ourCtx = FhirContext.forR4();
        IGenericClient ourClient = ourCtx.newRestfulGenericClient("http://localhost:9504/hapi-fhir-jpaserver/fhir/");

        int i=1;
        for ( String[] pd: patients){
            String id = "WORKLIST-"+i;
            Patient patient = (Patient) new Patient()
                    .addName(new HumanName().setFamily(pd[0]).addGiven(pd[1]))
                    .setId(id);
            ourClient.update().resource(patient).execute();

            ImagingStudy imagingStudy = (ImagingStudy) new ImagingStudy()
                    .setSubject( new Reference().setReference("Patient/"+id))
                    .setId(id);
            ourClient.update().resource(imagingStudy).execute();
            i++;
        }
        String id = "WORKLIST-"+i;
        ImagingStudy imagingStudy = (ImagingStudy) new ImagingStudy()
                .setId(id);
        ourClient.update().resource(imagingStudy).execute();
    }
}
