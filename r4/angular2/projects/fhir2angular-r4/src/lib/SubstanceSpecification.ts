import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { Reference } from './Reference'
import { SubstanceSpecification_Code } from './SubstanceSpecification_Code'
import { SubstanceSpecification_Moiety } from './SubstanceSpecification_Moiety'
import { SubstanceSpecification_MolecularWeight } from './SubstanceSpecification_MolecularWeight'
import { SubstanceSpecification_Name } from './SubstanceSpecification_Name'
import { SubstanceSpecification_Property } from './SubstanceSpecification_Property'
import { SubstanceSpecification_Relationship } from './SubstanceSpecification_Relationship'
import { SubstanceSpecification_Structure } from './SubstanceSpecification_Structure'

export class SubstanceSpecification      extends DomainResource
{

   static def : string = 'SubstanceSpecification';
   identifier : Identifier ;
   type : CodeableConcept ;
   status : CodeableConcept ;
   domain : CodeableConcept ;
   description : string ;
   source : Reference [];
   comment : string ;
   moiety : SubstanceSpecification_Moiety [];
   property : SubstanceSpecification_Property [];
   referenceInformation : Reference ;
   structure : SubstanceSpecification_Structure ;
   code : SubstanceSpecification_Code [];
   name : SubstanceSpecification_Name [];
   molecularWeight : SubstanceSpecification_MolecularWeight [];
   relationship : SubstanceSpecification_Relationship [];
   nucleicAcid : Reference ;
   polymer : Reference ;
   protein : Reference ;
   sourceMaterial : Reference ;
}
