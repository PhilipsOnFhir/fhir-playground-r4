import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Quantity } from './Quantity'
import { SpecimenDefinition_Additive } from './SpecimenDefinition_Additive'

export class SpecimenDefinition_Container      extends BackboneElement
{

   static def : string = 'SpecimenDefinition_Container';
   material : CodeableConcept ;
   type : CodeableConcept ;
   cap : CodeableConcept ;
   description : string ;
   capacity : Quantity ;
   minimumVolumeQuantity : Quantity ;
   minimumVolumeString : string ;
   additive : SpecimenDefinition_Additive [];
   preparation : string ;
}
