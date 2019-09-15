import { Attachment } from './Attachment'
import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { SubstanceNucleicAcid_Linkage } from './SubstanceNucleicAcid_Linkage'
import { SubstanceNucleicAcid_Sugar } from './SubstanceNucleicAcid_Sugar'

export class SubstanceNucleicAcid_Subunit      extends BackboneElement
{

   static def : string = 'SubstanceNucleicAcid_Subunit';
   subunit : string ;
   sequence : string ;
   length : string ;
   sequenceAttachment : Attachment ;
   fivePrime : CodeableConcept ;
   threePrime : CodeableConcept ;
   linkage : SubstanceNucleicAcid_Linkage [];
   sugar : SubstanceNucleicAcid_Sugar [];
}
