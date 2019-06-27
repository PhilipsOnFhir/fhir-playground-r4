import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { MedicinalProductIndication_OtherTherapy } from './MedicinalProductIndication_OtherTherapy'
import { Population } from './Population'
import { Quantity } from './Quantity'
import { Reference } from './Reference'

export class MedicinalProductIndication      extends DomainResource
{

   static def : string = 'MedicinalProductIndication';
   subject : Reference [];
   diseaseSymptomProcedure : CodeableConcept ;
   diseaseStatus : CodeableConcept ;
   comorbidity : CodeableConcept [];
   intendedEffect : CodeableConcept ;
   duration : Quantity ;
   otherTherapy : MedicinalProductIndication_OtherTherapy [];
   undesirableEffect : Reference [];
   population : Population [];
}
