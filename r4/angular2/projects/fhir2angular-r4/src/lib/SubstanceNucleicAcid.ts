import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { SubstanceNucleicAcid_Subunit } from './SubstanceNucleicAcid_Subunit'

export class SubstanceNucleicAcid      extends DomainResource
{

   static def : string = 'SubstanceNucleicAcid';
   sequenceType : CodeableConcept ;
   numberOfSubunits : string ;
   areaOfHybridisation : string ;
   oligoNucleotideType : CodeableConcept ;
   subunit : SubstanceNucleicAcid_Subunit [];
}
