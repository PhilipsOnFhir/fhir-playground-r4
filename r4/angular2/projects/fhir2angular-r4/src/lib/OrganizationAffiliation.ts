import { CodeableConcept } from './CodeableConcept'
import { ContactPoint } from './ContactPoint'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { Period } from './Period'
import { Reference } from './Reference'

export class OrganizationAffiliation      extends DomainResource
{

   static def : string = 'OrganizationAffiliation';
   identifier : Identifier [];
   active : boolean ;
   period : Period ;
   organization : Reference ;
   participatingOrganization : Reference ;
   network : Reference [];
   code : CodeableConcept [];
   specialty : CodeableConcept [];
   location : Reference [];
   healthcareService : Reference [];
   telecom : ContactPoint [];
   endpoint : Reference [];
}
