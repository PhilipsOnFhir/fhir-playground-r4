import { CatalogEntry_RelatedEntry } from './CatalogEntry_RelatedEntry'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { Period } from './Period'
import { PublicationStatusEnum } from './PublicationStatusEnum'
import { Reference } from './Reference'

export class CatalogEntry      extends DomainResource
{

   static def : string = 'CatalogEntry';
   identifier : Identifier [];
   type : CodeableConcept ;
   orderable : boolean ;
   referencedItem : Reference ;
   additionalIdentifier : Identifier [];
   classification : CodeableConcept [];
   status : PublicationStatusEnum ;
   validityPeriod : Period ;
   validTo : string ;
   lastUpdated : string ;
   additionalCharacteristic : CodeableConcept [];
   additionalClassification : CodeableConcept [];
   relatedEntry : CatalogEntry_RelatedEntry [];
}
