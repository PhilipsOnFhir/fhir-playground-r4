import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Quantity } from './Quantity'

export class InsurancePlan_Cost      extends BackboneElement
{

   static def : string = 'InsurancePlan_Cost';
   type : CodeableConcept ;
   applicability : CodeableConcept ;
   qualifiers : CodeableConcept [];
   value : Quantity ;
}
