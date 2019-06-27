import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { SpecimenContainedPreferenceEnum } from './SpecimenContainedPreferenceEnum'
import { SpecimenDefinition_Container } from './SpecimenDefinition_Container'
import { SpecimenDefinition_Handling } from './SpecimenDefinition_Handling'

export class SpecimenDefinition_TypeTested      extends BackboneElement
{

   static def : string = 'SpecimenDefinition_TypeTested';
   isDerived : boolean ;
   type : CodeableConcept ;
   preference : SpecimenContainedPreferenceEnum ;
   container : SpecimenDefinition_Container ;
   requirement : string ;
   retentionTime : string ;
   rejectionCriterion : CodeableConcept [];
   handling : SpecimenDefinition_Handling [];
}
