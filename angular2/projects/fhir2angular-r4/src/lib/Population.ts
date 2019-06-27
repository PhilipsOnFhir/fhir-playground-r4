import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Range } from './Range'

export class Population      extends BackboneElement
{

   static def : string = 'Population';
   ageRange : Range ;
   ageCodeableConcept : CodeableConcept ;
   gender : CodeableConcept ;
   race : CodeableConcept ;
   physiologicalCondition : CodeableConcept ;
}
