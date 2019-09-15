import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { Money } from './Money'
import { Reference } from './Reference'

export class PaymentReconciliation_Detail      extends BackboneElement
{

   static def : string = 'PaymentReconciliation_Detail';
   identifier : Identifier ;
   predecessor : Identifier ;
   type : CodeableConcept ;
   request : Reference ;
   submitter : Reference ;
   response : Reference ;
   date : string ;
   responsible : Reference ;
   payee : Reference ;
   amount : Money ;
}
