import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { MedicinalProductAuthorization_JurisdictionalAuthorization } from './MedicinalProductAuthorization_JurisdictionalAuthorization'
import { MedicinalProductAuthorization_Procedure } from './MedicinalProductAuthorization_Procedure'
import { Period } from './Period'
import { Reference } from './Reference'

export class MedicinalProductAuthorization      extends DomainResource
{

   static def : string = 'MedicinalProductAuthorization';
   identifier : Identifier [];
   subject : Reference ;
   country : CodeableConcept [];
   jurisdiction : CodeableConcept [];
   status : CodeableConcept ;
   statusDate : string ;
   restoreDate : string ;
   validityPeriod : Period ;
   dataExclusivityPeriod : Period ;
   dateOfFirstAuthorization : string ;
   internationalBirthDate : string ;
   legalBasis : CodeableConcept ;
   jurisdictionalAuthorization : MedicinalProductAuthorization_JurisdictionalAuthorization [];
   holder : Reference ;
   regulator : Reference ;
   procedure : MedicinalProductAuthorization_Procedure ;
}
