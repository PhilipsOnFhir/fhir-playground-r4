package org.github.philipsonfhir.smartsuite.fhircast.server.service.util;

import org.github.philipsonfhir.smartsuite.fhircast.support.FhirCastException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class Hmac {

    public static String calculateHMAC(String secret, String content ) throws FhirCastException {
        Mac mac;
        try {
            mac = Mac.getInstance("HmacSHA256");
            String magicKey = "hellothere";
            mac.init(new SecretKeySpec(magicKey.getBytes(), "HmacSHA256"));

            byte[] hash = mac.doFinal(secret.getBytes());
            return DatatypeConverter.printHexBinary(hash);
        } catch ( Exception e ) {
            throw new FhirCastException( "Error generating HMAC" );
        }
    }
}
