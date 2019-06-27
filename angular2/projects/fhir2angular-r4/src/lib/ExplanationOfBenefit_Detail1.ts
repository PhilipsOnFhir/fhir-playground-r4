import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { ExplanationOfBenefit_Adjudication } from './ExplanationOfBenefit_Adjudication'
import { ExplanationOfBenefit_SubDetail1 } from './ExplanationOfBenefit_SubDetail1'
import { Money } from './Money'
import { Quantity } from './Quantity'

export class ExplanationOfBenefit_Detail1      extends BackboneElement
{

   static def : string = 'ExplanationOfBenefit_Detail1';
   productOrService : CodeableConcept ;
   modifier : CodeableConcept [];
   quantity : Quantity ;
   unitPrice : Money ;
   factor : string ;
   net : Money ;
   noteNumber : string [];
   adjudication : ExplanationOfBenefit_Adjudication [];
   subDetail : ExplanationOfBenefit_SubDetail1 [];
}
