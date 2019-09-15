import { BackboneElement } from './BackboneElement'
import { ClaimResponse_Adjudication } from './ClaimResponse_Adjudication'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Money } from './Money'
import { Quantity } from './Quantity'

export class ClaimResponse_SubDetail1      extends BackboneElement
{

   static def : string = 'ClaimResponse_SubDetail1';
   productOrService : CodeableConcept ;
   modifier : CodeableConcept [];
   quantity : Quantity ;
   unitPrice : Money ;
   factor : string ;
   net : Money ;
   noteNumber : string [];
   adjudication : ClaimResponse_Adjudication [];
}
