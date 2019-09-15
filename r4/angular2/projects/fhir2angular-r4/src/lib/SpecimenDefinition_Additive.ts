import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Reference } from './Reference'

export class SpecimenDefinition_Additive      extends BackboneElement
{

   static def : string = 'SpecimenDefinition_Additive';
   additiveCodeableConcept : CodeableConcept ;
   additiveReference : Reference ;
}
