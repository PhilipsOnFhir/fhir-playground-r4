package org.github.philipsonfhir.fhirproxy.testutil;

import org.hl7.fhir.r4.model.HumanName;

import java.util.Random;

public class NameGenerator {
    static final String[] fruits = {"aalbes", "aardbei", "aardbeiboom (Arbutus)", "aardbeiguave", "abrikoos", "acerola", "Afrikaanse baobab", "aki", "alibertia", "ambarella", "Amerikaanse mammi-appel", "Amerikaanse persimoen", "ananas", "appel", "appelbanaan", "aprium", "arazá", "atemoya", "aubergine", "avocado", "awarra", "azarole", "babaco (chamburo)", "banaan", "bauno (binjai)", "bergamot", "bergpapaja", "bergzuurzak", "bes", "biribá", "blauwe bes", "blauwe bosbes", "blimbing", "bloedsinaasappel", "braam", "broodvrucht", "cactusvijg (woestijnvijg)", "cainito", "calamondin", "canistel", "cantaloupe", "carambola (stervrucht)", "cashewappel", "cassabanana", "cherimoya", "chilipeper", "chinapeer", "Chinese kumquat", "citroen", "citrusvrucht", "clementine", "coronilla", "Costa Rica-guave", "courgette", "cranberry (grote veenbes)", "curuba (bananenpassievrucht, tacso)", "custardappel (ossenhart)", "dadel", "djamboe aer", "djamboe aer mawar", "djamboe bol", "djamboe semarang", "doerian", "druif", "framboos", "feijoa", "gandaria", "gatenplantvrucht", "gele mombinpruim", "genipapo", "goudbes (ananaskers, Kaapse kruisbes)", "granaatappel", "grapefruit", "grosella", "guave (djamboe kloetoek)", "honingmeloen", "ikakopruim", "jaboticaba", "jackfruit (nangka)", "jakhalsbes", "jambolan", "Japanse wijnbes", "jujube", "kaki", "kapoelasan", "kei-appel", "kepel", "kers", "ketembilla", "kiwano", "kiwi", "knippa", "kokosnoot", "komkommer", "korlan", "kruisbes", "kumquat", "kweepeer", "kwini", "langsat", "lijsterbes", "limoen (lime, lemmetje)", "longan", "loquat", "lotusvrucht", "lulo", "lychee", "mabolo", "mamey sapota", "mandarijn", "mangistan", "mango", "marang", "meloen", "Mexicaanse aardkers", "mini-kiwi", "mispel", "moendoe", "moerbei", "moriche", "nance", "nashipeer", "natalpruim", "nectarine", "noni", "olifantsappel", "olijf", "orinoco-appel", "papaja", "papeda", "paprika", "passievrucht (maracuja, granadilla)", "pawpaw", "peer", "pepino (meloenpeer)", "perzik", "perzikpalm", "pitaja", "pitomba I", "pitomba II", "pluot", "pomelo", "pompelmoes", "prachtframboos", "prachtige mango", "pruim", "ramboetan", "rijsbes", "rode bes", "rode bosbes", "rode moerbei", "rode mombinpruim", "rozenbottel", "salak", "santol", "sapodilla", "satsuma", "schroefpalm", "sharonfruit", "sinaasappel", "slijmappel", "soncoya", "stervrucht", "stinkende mango", "stranddruif", "Surinaamse kers", "sweetie", "switbonki", "tamarillo (boomtomaat)", "tjampedak", "tomaat", "tomatillo", "ugli", "vijg", "vlierbes", "wampi", "witte bes", "witte moerbei", "witte zapote", "watermeloen", "yang mei", "zoete kers", "zoete passievrucht", "zoetzak (suikerappel)", "zure kers", "zuurzak (guanábana)", "zwarte bes (cassis)", "zwarte moerbei", "zwarte zapote"};
    static final String[] vegetables = { "aardaker", "aardamandel, knolcyperus", "aardappel", "aardpeer, topinamboer", "abc-kruid, champagneblad", "adelaarsvaren", "Afrikaanse baobab", "alfalfa, luzerne", "alocasie, reuzentaro", "amsoi", "andijvie", "antruwa", "arrowroot", "artisjok", "asperge", "asperge-erwt", "aubergine", "augurk", "ayote", "bamboescheut, bamboespruit", "bieslook", "biet", "bindsla", "bleekselderij", "bloemkool", "boomkalebas", "boon", "boerenkool", "bosui", "brandnetel", "brave hendrik", "broccoli", "broodvrucht", "bimi", "cassave, maniok", "cayennepeper", "chayote", "chilipeper", "chinese kool", "citroengras", "courgette", "dok kae, Sesbania grandiflora", "doperwt, erwt", "duizendkoppige kool, duizendkop of duizendknop", "eeuwig moes, duizendkoppige kool", "erwt", "eikenbladsla", "fleskalebas", "flespompoen", "groene kool, savooiekool", "groenlof", "haverwortel, paarse morgenster, boksbaard", "hertshoornweegbree", "ijsbergsla", "ijskruid", "ingerolde palmvaren", "jackfruit, nangka", "kailan", "kannibaaltomaat", "kappertje", "kapucijner", "kardoen", "kervel", "kikkererwt, keker, garbanzo", "kiwano", "kliswortel, klit", "knolcapucien", "knolkervel, knolribzaad", "knoflook", "knolraap, meiknolletje", "knolselderij", "knolvenkel, venkel", "komkommer", "konjak", "kool", "koolraap", "koolrabi", "kousenband", "kudzu", "lamsoor, zulte, zeeaster", "lente-ui, bosui", "linzen", "lotuswortel, heilige lotus", "mais", "meiraap, knolraap", "molsla, paardenbloem", "melindjoe", "mergkool", "mungboon", "muskaatpompoen", "nierboon", "Nieuw-Zeelandse spinazie", "oca", "okra", "olifantenyam", "olijf", "paksoi", "palmkool (cavolo nero, zwarte kool)", "paprika", "pastinaak", "patisson", "peen", "pereskia", "peterselie", "peultjes", "pijlkruid", "pijpajuin", "pompoen", "postelein", "prei", "pronkboon", "raapstelen, bladmoes", "rabarber", "radijs", "rammenas, ook wel rettich genoemd", "repelsteeltje", "rode biet, kroot", "rodekool", "romanesco", "roodlof", "rucola, raketsla", "sago", "savooiekool", "schorseneren", "selderij, selderie", "sint-jansuitjes", "sjalotten", "sla", "snijbiet", "snijboon", "snijselderij", "spekboon", "sopropo, paré", "sperzieboon", "spinazie", "spinaziezuring", "spitskool (wittekool of savooiekool)", "splijtkool, duizendkoppige kool", "sponskomkommer, nenwa", "spruitkool", "suikerbiet", "suikermais", "suikerwortel", "sojaboon", "takako", "taro", "taugé", "tayer", "tetragonia (Nieuw-Zeelandse spinazie)", "teunisbloem", "tindola, papasan", "tomaat", "topinamboer, aardpeer", "tuinkers", "tuinboon", "tuinmelde", "veldsla", "veldzuring", "venkel", "vijgenbladpompoen", "vleugelkomkommer", "vleugelboon", "waspompoen", "witte waterkers", "waterspinazie", "winterpostelein", "witlof", "wittekool", "witte mimosa", "wortel", "wortelpeterselie", "yacón", "yam", "yamboon", "zeekool", "zeekraal", "zoete aardappel, bataat", "zonnewortel", "zuring", "zuurkool", "zwarte kool (cavolo nero, palmkool)" };
    static Random random = new Random( System.currentTimeMillis() );

    public static HumanName createPatientHumanName(){
        return createHumanName(fruits);
    }

    public static HumanName createPractitionerHumanName() {
        return createHumanName(vegetables);
    }

    private static HumanName createHumanName( String[] names ){

        HumanName humanName = new HumanName();
        String familyName = names[random.nextInt(names.length)];
        familyName = familyName.substring(0,1).toUpperCase()+familyName.substring(1);

        String givenName = names[random.nextInt(names.length)];
        givenName = givenName.substring(0,1).toUpperCase()+givenName.substring(1);

        humanName.setFamily( familyName );
        humanName.addGiven( givenName );

        return humanName;
    }

}
