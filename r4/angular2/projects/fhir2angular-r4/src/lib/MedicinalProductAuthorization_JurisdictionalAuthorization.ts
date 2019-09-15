import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { Period } from './Period'

export class MedicinalProductAuthorization_JurisdictionalAuthorization      extends BackboneElement
{

   static def : string = 'MedicinalProductAuthorization_JurisdictionalAuthorization';
   identifier : Identifier [];
   country : CodeableConcept ;
   jurisdiction : CodeableConcept [];
   legalStatusOfSupply : CodeableConcept ;
   validityPeriod : Period ;
}
