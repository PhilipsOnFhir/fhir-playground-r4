import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Timing_Repeat } from './Timing_Repeat'

export class Timing      extends BackboneElement
{

   static def : string = 'Timing';
   event : string [];
   repeat : Timing_Repeat ;
   code : CodeableConcept ;
}
