import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { MedicinalProductContraindication_OtherTherapy } from './MedicinalProductContraindication_OtherTherapy'
import { Population } from './Population'
import { Reference } from './Reference'

export class MedicinalProductContraindication      extends DomainResource
{

   static def : string = 'MedicinalProductContraindication';
   subject : Reference [];
   disease : CodeableConcept ;
   diseaseStatus : CodeableConcept ;
   comorbidity : CodeableConcept [];
   therapeuticIndication : Reference [];
   otherTherapy : MedicinalProductContraindication_OtherTherapy [];
   population : Population [];
}
