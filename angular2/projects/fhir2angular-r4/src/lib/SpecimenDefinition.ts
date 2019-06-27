import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { SpecimenDefinition_TypeTested } from './SpecimenDefinition_TypeTested'

export class SpecimenDefinition      extends DomainResource
{

   static def : string = 'SpecimenDefinition';
   identifier : Identifier ;
   typeCollected : CodeableConcept ;
   patientPreparation : CodeableConcept [];
   timeAspect : string ;
   collection : CodeableConcept [];
   typeTested : SpecimenDefinition_TypeTested [];
}
