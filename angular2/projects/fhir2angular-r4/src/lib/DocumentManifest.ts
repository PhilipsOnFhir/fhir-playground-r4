import { CodeableConcept } from './CodeableConcept'
import { DocumentManifest_Related } from './DocumentManifest_Related'
import { DocumentReferenceStatusEnum } from './DocumentReferenceStatusEnum'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { Reference } from './Reference'

export class DocumentManifest      extends DomainResource
{

   static def : string = 'DocumentManifest';
   masterIdentifier : Identifier ;
   identifier : Identifier [];
   status : DocumentReferenceStatusEnum ;
   type : CodeableConcept ;
   subject : Reference ;
   created : string ;
   author : Reference [];
   recipient : Reference [];
   source : string ;
   description : string ;
   content : Reference [];
   related : DocumentManifest_Related [];
}
