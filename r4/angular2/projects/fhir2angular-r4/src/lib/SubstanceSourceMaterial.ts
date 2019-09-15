import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { SubstanceSourceMaterial_FractionDescription } from './SubstanceSourceMaterial_FractionDescription'
import { SubstanceSourceMaterial_Organism } from './SubstanceSourceMaterial_Organism'
import { SubstanceSourceMaterial_PartDescription } from './SubstanceSourceMaterial_PartDescription'

export class SubstanceSourceMaterial      extends DomainResource
{

   static def : string = 'SubstanceSourceMaterial';
   sourceMaterialClass : CodeableConcept ;
   sourceMaterialType : CodeableConcept ;
   sourceMaterialState : CodeableConcept ;
   organismId : Identifier ;
   organismName : string ;
   parentSubstanceId : Identifier [];
   parentSubstanceName : string [];
   countryOfOrigin : CodeableConcept [];
   geographicalLocation : string [];
   developmentStage : CodeableConcept ;
   fractionDescription : SubstanceSourceMaterial_FractionDescription [];
   organism : SubstanceSourceMaterial_Organism ;
   partDescription : SubstanceSourceMaterial_PartDescription [];
}
