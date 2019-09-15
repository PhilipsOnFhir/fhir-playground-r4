import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { ExplanationOfBenefit_Adjudication } from './ExplanationOfBenefit_Adjudication'
import { Money } from './Money'
import { Quantity } from './Quantity'

export class ExplanationOfBenefit_SubDetail1      extends BackboneElement
{

   static def : string = 'ExplanationOfBenefit_SubDetail1';
   productOrService : CodeableConcept ;
   modifier : CodeableConcept [];
   quantity : Quantity ;
   unitPrice : Money ;
   factor : string ;
   net : Money ;
   noteNumber : string [];
   adjudication : ExplanationOfBenefit_Adjudication [];
}
