import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'

export class SubstanceSourceMaterial_OrganismGeneral      extends BackboneElement
{

   static def : string = 'SubstanceSourceMaterial_OrganismGeneral';
   kingdom : CodeableConcept ;
   phylum : CodeableConcept ;
   class : CodeableConcept ;
   order : CodeableConcept ;
}
