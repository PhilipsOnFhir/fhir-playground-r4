import { Annotation } from './Annotation'
import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { Contract_Subject } from './Contract_Subject'
import { DomainResource } from './DomainResource'
import { Period } from './Period'
import { Reference } from './Reference'
import { Timing } from './Timing'

export class Contract_Action      extends BackboneElement
{

   static def : string = 'Contract_Action';
   doNotPerform : boolean ;
   type : CodeableConcept ;
   subject : Contract_Subject [];
   intent : CodeableConcept ;
   linkId : string [];
   status : CodeableConcept ;
   context : Reference ;
   contextLinkId : string [];
   occurrenceDateTime : string ;
   occurrencePeriod : Period ;
   occurrenceTiming : Timing ;
   requester : Reference [];
   requesterLinkId : string [];
   performerType : CodeableConcept [];
   performerRole : CodeableConcept ;
   performer : Reference ;
   performerLinkId : string [];
   reasonCode : CodeableConcept [];
   reasonReference : Reference [];
   reason : string [];
   reasonLinkId : string [];
   note : Annotation [];
   securityLabelNumber : string [];
}
