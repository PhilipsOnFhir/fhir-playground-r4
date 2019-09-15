import { Address } from './Address'
import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { ExplanationOfBenefit_Adjudication } from './ExplanationOfBenefit_Adjudication'
import { ExplanationOfBenefit_Detail1 } from './ExplanationOfBenefit_Detail1'
import { Money } from './Money'
import { Period } from './Period'
import { Quantity } from './Quantity'
import { Reference } from './Reference'

export class ExplanationOfBenefit_AddItem      extends BackboneElement
{

   static def : string = 'ExplanationOfBenefit_AddItem';
   itemSequence : string [];
   detailSequence : string [];
   subDetailSequence : string [];
   provider : Reference [];
   productOrService : CodeableConcept ;
   modifier : CodeableConcept [];
   programCode : CodeableConcept [];
   servicedDate : string ;
   servicedPeriod : Period ;
   locationCodeableConcept : CodeableConcept ;
   locationAddress : Address ;
   locationReference : Reference ;
   quantity : Quantity ;
   unitPrice : Money ;
   factor : string ;
   net : Money ;
   bodySite : CodeableConcept ;
   subSite : CodeableConcept [];
   noteNumber : string [];
   adjudication : ExplanationOfBenefit_Adjudication [];
   detail : ExplanationOfBenefit_Detail1 [];
}
