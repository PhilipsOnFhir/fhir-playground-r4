import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { ContractResourcePublicationStatusCodesEnum } from './ContractResourcePublicationStatusCodesEnum'
import { DomainResource } from './DomainResource'
import { Reference } from './Reference'

export class Contract_ContentDefinition      extends BackboneElement
{

   static def : string = 'Contract_ContentDefinition';
   type : CodeableConcept ;
   subType : CodeableConcept ;
   publisher : Reference ;
   publicationDate : string ;
   publicationStatus : ContractResourcePublicationStatusCodesEnum ;
   copyright : string ;
}
