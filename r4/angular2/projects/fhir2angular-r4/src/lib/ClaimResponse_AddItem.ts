import { Address } from './Address'
import { BackboneElement } from './BackboneElement'
import { ClaimResponse_Adjudication } from './ClaimResponse_Adjudication'
import { ClaimResponse_Detail1 } from './ClaimResponse_Detail1'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Money } from './Money'
import { Period } from './Period'
import { Quantity } from './Quantity'
import { Reference } from './Reference'

export class ClaimResponse_AddItem      extends BackboneElement
{

   static def : string = 'ClaimResponse_AddItem';
   itemSequence : string [];
   detailSequence : string [];
   subdetailSequence : string [];
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
   adjudication : ClaimResponse_Adjudication [];
   detail : ClaimResponse_Detail1 [];
}
