import { BackboneElement } from './BackboneElement'
import { DomainResource } from './DomainResource'
import { EventTimingEnum } from './EventTimingEnum'
import { Period } from './Period'
import { Range } from './Range'
import { UnitsOfTimeEnum } from './UnitsOfTimeEnum'

export class Timing_Repeat      extends BackboneElement
{

   static def : string = 'Timing_Repeat';
   boundsDuration : string ;
   boundsRange : Range ;
   boundsPeriod : Period ;
   count : string ;
   countMax : string ;
   duration : string ;
   durationMax : string ;
   durationUnit : UnitsOfTimeEnum ;
   frequency : string ;
   frequencyMax : string ;
   period : string ;
   periodMax : string ;
   periodUnit : UnitsOfTimeEnum ;
   dayOfWeek : string [];
   timeOfDay : string [];
   when : EventTimingEnum [];
   offset : string ;
}
