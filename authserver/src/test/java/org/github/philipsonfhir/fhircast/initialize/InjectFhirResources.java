package org.github.philipsonfhir.fhircast.initialize;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.*;
import org.junit.Test;

import java.util.Random;

public class InjectFhirResources {
    String[] groentes = {"aardaker", "aardamandel", "knolcyperus", "aardappel", "aardpeer", "topinamboer", "abc-kruid", "champagneblad", "adelaarsvaren", "Afrikaanse baobab", "alfalfa", "luzerne", "alocasie", "reuzentaro", "amsoi", "andijvie", "antruwa", "arrowroot", "artisjok", "asperge", "asperge-erwt", "aubergine", "augurk", "ayote", "B", "bamboescheut", "bamboespruit", "bieslook", "biet", "bindsla", "bleekselderij", "bloemkool", "boomkalebas", "boon", "boerenkool", "bosui", "brandnetel", "brave hendrik", "broccoli", "broodvrucht", "bimi", "C", "cassave", "maniok", "cayennepeper", "chayote", "chilipeper", "chinese kool", "citroengras", "courgette", "D", "dok kae", "Sesbania grandiflora", "doperwt", "erwt", "duizendkoppige kool", "duizendkop of duizendknop", "E", "eeuwig moes", "duizendkoppige kool", "erwt", "eikenbladsla", "F", "fleskalebas", "flespompoen", "G", "groene kool", "savooiekool", "groenlof", "H", "haverwortel", "paarse morgenster", "boksbaard", "hertshoornweegbree", "I", "ijsbergsla", "ijskruid", "ingerolde palmvaren", "J", "jackfruit", "nangka", "K", "kailan", "kannibaaltomaat", "kappertje", "kapucijner", "kardoen", "kervel", "kikkererwt", "keker", "garbanzo", "kiwano", "kliswortel", "klit", "knolcapucien", "knolkervel", "knolribzaad", "knoflook", "knolraap", "meiknolletje", "knolselderij", "knolvenkel", "venkel", "komkommer", "konjak", "kool", "koolraap", "koolrabi", "kousenband", "kudzu", "L", "lamsoor", "zulte", "zeeaster", "lente-ui", "bosui", "linzen", "lotuswortel", "heilige lotus", "M", "mais", "meiraap", "knolraap", "molsla", "paardenbloem", "melindjoe", "mergkool", "mungboon", "muskaatpompoen", "N", "nierboon", "Nieuw-Zeelandse spinazie", "O", "oca", "okra", "olifantenyam", "olijf", "P", "paksoi", "palmkool (cavolo nero", "zwarte kool)", "paprika", "pastinaak", "patisson", "peen", "pereskia", "peterselie", "peultjes", "pijlkruid", "pijpajuin", "pompoen", "postelein", "prei", "pronkboon", "Q", "R", "raapstelen", "bladmoes", "rabarber", "radijs", "rammenas", "ook wel rettich genoemd", "repelsteeltje", "rode biet", "kroot", "rodekool", "romanesco", "roodlof", "rucola", "raketsla", "S", "sago", "savooiekool", "schorseneren", "selderij", "selderie", "sint-jansuitjes", "sjalotten", "sla", "snijbiet", "snijboon", "snijselderij", "spekboon", "sopropo", "paré", "sperzieboon", "spinazie", "spinaziezuring", "spitskool (wittekool of savooiekool)", "splijtkool", "duizendkoppige kool", "sponskomkommer", "nenwa", "spruitkool", "suikerbiet", "suikermais", "suikerwortel", "sojaboon", "T", "takako", "taro", "taugé", "tayer", "tetragonia (Nieuw-Zeelandse spinazie)", "teunisbloem", "tindola", "papasan", "tomaat", "topinamboer", "aardpeer", "tuinkers", "tuinboon", "tuinmelde", "U", "ui", "V", "veldsla", "veldzuring", "venkel", "vijgenbladpompoen", "vleugelkomkommer", "vleugelboon", "W", "waspompoen", "witte waterkers", "waterspinazie", "winterpostelein", "witlof", "wittekool", "witte mimosa", "wortel", "wortelpeterselie", "X", "Y", "yacón", "yam", "yamboon", "Z", "zeekool", "zeekraal", "zoete aardappel", "bataat", "zonnewortel", "zuring", "zuurkool", "zwarte kool (cavolo nero", "palmkool"};
    String[] fruit    = {"aalbes", "aardbei", "aardbeiboom (Arbutus)", "aardbeiguave", "abrikoos", "acerola", "Afrikaanse baobab", "aki", "alibertia", "ambarella", "Amerikaanse mammi-appel", "Amerikaanse persimoen", "ananas", "appel", "appelbanaan", "aprium", "arazá", "atemoya", "aubergine", "avocado", "awarra", "azarole", "B", "babaco (chamburo)", "banaan", "bauno (binjai)", "bergamot", "bergpapaja", "bergzuurzak", "bes", "biribá", "blauwe bes", "blauwe bosbes", "blimbing", "bloedsinaasappel", "braam", "broodvrucht", "C", "cactusvijg (woestijnvijg)", "cainito", "calamondin", "canistel", "cantaloupe", "carambola (stervrucht)", "cashewappel", "cassabanana", "cherimoya", "chilipeper", "chinapeer", "Chinese kumquat", "citroen", "citrusvrucht", "clementine", "coronilla", "Costa Rica-guave", "courgette", "cranberry (grote veenbes)", "curuba (bananenpassievrucht, tacso)", "custardappel (ossenhart)", "D", "dadel", "djamboe aer", "djamboe aer mawar", "djamboe bol", "djamboe semarang", "doerian", "druif", "E", "F", "framboos", "feijoa", "G", "gandaria", "gatenplantvrucht", "gele mombinpruim", "genipapo", "goudbes (ananaskers, Kaapse kruisbes)", "granaatappel", "grapefruit", "grosella", "guave (djamboe kloetoek)", "H", "honingmeloen", "I", "ikakopruim", "J", "jaboticaba", "jackfruit (nangka)", "jakhalsbes", "jambolan", "Japanse wijnbes", "jujube", "K", "kaki", "kapoelasan", "kei-appel", "kepel", "kers", "ketembilla", "kiwano", "kiwi", "knippa", "kokosnoot", "komkommer", "korlan", "kruisbes", "kumquat", "kweepeer", "kwini", "L", "langsat", "lijsterbes", "limoen (lime, lemmetje)", "longan", "loquat", "lotusvrucht", "lulo", "lychee", "M", "mabolo", "mamey sapota", "mandarijn", "mangistan", "mango", "marang", "meloen", "Mexicaanse aardkers", "mini-kiwi", "mispel", "moendoe", "moerbei", "moriche", "N", "nance", "nashipeer", "natalpruim", "nectarine", "noni", "O", "olifantsappel", "olijf", "orinoco-appel", "P", "papaja", "papeda", "paprika", "passievrucht (maracuja, granadilla)", "pawpaw", "peer", "pepino (meloenpeer)", "perzik", "perzikpalm", "pitaja", "pitomba I", "pitomba II", "pluot", "pomelo", "pompelmoes", "prachtframboos", "prachtige mango", "pruim", "Q", "R", "ramboetan", "rijsbes", "rode bes", "rode bosbes", "rode moerbei", "rode mombinpruim", "rozenbottel", "S", "salak", "santol", "sapodilla", "satsuma", "schroefpalm", "sharonfruit", "sinaasappel", "slijmappel", "soncoya", "stervrucht", "stinkende mango", "stranddruif", "Surinaamse kers", "sweetie", "switbonki", "T", "tamarillo (boomtomaat)", "tjampedak", "tomaat", "tomatillo", "U", "ugli", "V", "vijg", "vlierbes", "W", "wampi", "witte bes", "witte moerbei", "witte zapote", "watermeloen", "X Y Z", "yang mei", "zoete kers", "zoete passievrucht", "zoetzak (suikerappel)", "zure kers", "zuurzak (guanábana)", "zwarte bes (cassis)", "zwarte moerbei", "zwarte zapote"};

    @Test
    public void injectPatientResources(){
        Random random = new Random();
        random.nextInt(groentes.length);

        FhirContext ourCtx = FhirContext.forR4();
        IGenericClient ourClient = ourCtx.newRestfulGenericClient("http://localhost:9504/hapi-fhir-jpaserver/fhir/");
        {
            String[][] practioners = new String[7][2];
            for ( int i=0; i<7; i++ ){
                practioners[i][0] = StringUtils.capitalize( fruit[ random.nextInt(fruit.length)] );
                practioners[i][1] = StringUtils.capitalize( fruit[ random.nextInt(fruit.length)] );
            }
            String[] loginNames = {"practitioner", "nurse" };

            int i = 1;
            for (String[] pd : practioners) {
                String id = "WORKLIST-" + i;
                Practitioner practitioner = (Practitioner) new Practitioner()
                        .addName(new HumanName().setFamily(pd[0]).addGiven(pd[1]))
                        .setId(id);
                if ( i-1<loginNames.length ){
                    practitioner.addIdentifier(
                            new Identifier()
                                .setSystem("http://github.com/philipsonfhir/loginid")
                                .setValue( loginNames[i-1])
                    );
                }
                ourClient.update().resource(practitioner).execute();

                ImagingStudy imagingStudy = (ImagingStudy) new ImagingStudy()
                        .setSubject(new Reference().setReference("Patient/" + id))
                        .setId(id);
                ourClient.update().resource(imagingStudy).execute();
                i++;
            }
        }
        {
            String[][] patients = new String[7][2];
            for ( int i=0; i<7; i++ ){
                patients[i][0] = StringUtils.capitalize( groentes[ random.nextInt(groentes.length)] );
                patients[i][1] = StringUtils.capitalize( groentes[ random.nextInt(groentes.length)] );
            }

            int i = 1;
            for (String[] pd : patients) {
                String id = "WORKLIST-" + i;
                Patient patient = (Patient) new Patient()
                        .addName(new HumanName().setFamily(pd[0]).addGiven(pd[1]))
                        .setId(id);
                ourClient.update().resource(patient).execute();

                ImagingStudy imagingStudy = (ImagingStudy) new ImagingStudy()
                        .setSubject(new Reference().setReference("Patient/" + id))
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

}
