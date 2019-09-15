import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'

export class HealthcareService_Eligibility      extends BackboneElement
{

   static def : string = 'HealthcareService_Eligibility';
   code : CodeableConcept ;
   comment : string ;
}
