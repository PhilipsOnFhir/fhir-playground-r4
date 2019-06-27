import { Address } from './Address'
import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { ContactPoint } from './ContactPoint'
import { DomainResource } from './DomainResource'
import { HumanName } from './HumanName'

export class InsurancePlan_Contact      extends BackboneElement
{

   static def : string = 'InsurancePlan_Contact';
   purpose : CodeableConcept ;
   name : HumanName ;
   telecom : ContactPoint [];
   address : Address ;
}
