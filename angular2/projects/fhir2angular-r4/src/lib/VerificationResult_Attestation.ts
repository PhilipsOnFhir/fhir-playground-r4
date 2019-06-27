import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Reference } from './Reference'
import { Signature } from './Signature'

export class VerificationResult_Attestation      extends BackboneElement
{

   static def : string = 'VerificationResult_Attestation';
   who : Reference ;
   onBehalfOf : Reference ;
   communicationMethod : CodeableConcept ;
   date : string ;
   sourceIdentityCertificate : string ;
   proxyIdentityCertificate : string ;
   proxySignature : Signature ;
   sourceSignature : Signature ;
}
