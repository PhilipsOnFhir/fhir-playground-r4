import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { SubstanceSourceMaterial_Author } from './SubstanceSourceMaterial_Author'
import { SubstanceSourceMaterial_Hybrid } from './SubstanceSourceMaterial_Hybrid'
import { SubstanceSourceMaterial_OrganismGeneral } from './SubstanceSourceMaterial_OrganismGeneral'

export class SubstanceSourceMaterial_Organism      extends BackboneElement
{

   static def : string = 'SubstanceSourceMaterial_Organism';
   family : CodeableConcept ;
   genus : CodeableConcept ;
   species : CodeableConcept ;
   intraspecificType : CodeableConcept ;
   intraspecificDescription : string ;
   author : SubstanceSourceMaterial_Author [];
   hybrid : SubstanceSourceMaterial_Hybrid ;
   organismGeneral : SubstanceSourceMaterial_OrganismGeneral ;
}
