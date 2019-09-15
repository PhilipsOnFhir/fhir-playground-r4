import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'

export class SubstanceSourceMaterial_Author      extends BackboneElement
{

   static def : string = 'SubstanceSourceMaterial_Author';
   authorType : CodeableConcept ;
   authorDescription : string ;
}
