import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'

export class SubstanceSourceMaterial_PartDescription      extends BackboneElement
{

   static def : string = 'SubstanceSourceMaterial_PartDescription';
   part : CodeableConcept ;
   partLocation : CodeableConcept ;
}
