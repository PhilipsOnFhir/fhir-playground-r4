import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Population } from './Population'
import { Reference } from './Reference'

export class MedicinalProductUndesirableEffect      extends DomainResource
{

   static def : string = 'MedicinalProductUndesirableEffect';
   subject : Reference [];
   symptomConditionEffect : CodeableConcept ;
   classification : CodeableConcept ;
   frequencyOfOccurrence : CodeableConcept ;
   population : Population [];
}
