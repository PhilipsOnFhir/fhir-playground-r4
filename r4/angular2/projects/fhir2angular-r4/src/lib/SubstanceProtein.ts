import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { SubstanceProtein_Subunit } from './SubstanceProtein_Subunit'

export class SubstanceProtein      extends DomainResource
{

   static def : string = 'SubstanceProtein';
   sequenceType : CodeableConcept ;
   numberOfSubunits : string ;
   disulfideLinkage : string [];
   subunit : SubstanceProtein_Subunit [];
}
