import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { MarketingStatus } from './MarketingStatus'
import { MedicinalProductPackaged_BatchIdentifier } from './MedicinalProductPackaged_BatchIdentifier'
import { MedicinalProductPackaged_PackageItem } from './MedicinalProductPackaged_PackageItem'
import { Reference } from './Reference'

export class MedicinalProductPackaged      extends DomainResource
{

   static def : string = 'MedicinalProductPackaged';
   identifier : Identifier [];
   subject : Reference [];
   description : string ;
   legalStatusOfSupply : CodeableConcept ;
   marketingStatus : MarketingStatus [];
   marketingAuthorization : Reference ;
   manufacturer : Reference [];
   batchIdentifier : MedicinalProductPackaged_BatchIdentifier [];
   packageItem : MedicinalProductPackaged_PackageItem [];
}
