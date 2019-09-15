import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Quantity } from './Quantity'

export class InsurancePlan_Limit      extends BackboneElement
{

   static def : string = 'InsurancePlan_Limit';
   value : Quantity ;
   code : CodeableConcept ;
}
