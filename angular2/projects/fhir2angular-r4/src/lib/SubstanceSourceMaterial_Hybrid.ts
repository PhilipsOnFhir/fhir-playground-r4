import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'

export class SubstanceSourceMaterial_Hybrid      extends BackboneElement
{

   static def : string = 'SubstanceSourceMaterial_Hybrid';
   maternalOrganismId : string ;
   maternalOrganismName : string ;
   paternalOrganismId : string ;
   paternalOrganismName : string ;
   hybridType : CodeableConcept ;
}
