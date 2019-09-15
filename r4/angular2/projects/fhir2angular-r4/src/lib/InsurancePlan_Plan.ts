import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { InsurancePlan_GeneralCost } from './InsurancePlan_GeneralCost'
import { InsurancePlan_SpecificCost } from './InsurancePlan_SpecificCost'
import { Reference } from './Reference'

export class InsurancePlan_Plan      extends BackboneElement
{

   static def : string = 'InsurancePlan_Plan';
   identifier : Identifier [];
   type : CodeableConcept ;
   coverageArea : Reference [];
   network : Reference [];
   generalCost : InsurancePlan_GeneralCost [];
   specificCost : InsurancePlan_SpecificCost [];
}
