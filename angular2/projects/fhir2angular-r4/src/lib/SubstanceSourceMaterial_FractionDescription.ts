import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'

export class SubstanceSourceMaterial_FractionDescription      extends BackboneElement
{

   static def : string = 'SubstanceSourceMaterial_FractionDescription';
   fraction : string ;
   materialType : CodeableConcept ;
}
