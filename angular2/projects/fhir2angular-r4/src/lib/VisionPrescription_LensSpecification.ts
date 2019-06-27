import { Annotation } from './Annotation'
import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Quantity } from './Quantity'
import { VisionEyesEnum } from './VisionEyesEnum'
import { VisionPrescription_Prism } from './VisionPrescription_Prism'

export class VisionPrescription_LensSpecification      extends BackboneElement
{

   static def : string = 'VisionPrescription_LensSpecification';
   product : CodeableConcept ;
   eye : VisionEyesEnum ;
   sphere : string ;
   cylinder : string ;
   axis : string ;
   prism : VisionPrescription_Prism [];
   add : string ;
   power : string ;
   backCurve : string ;
   diameter : string ;
   duration : Quantity ;
   color : string ;
   brand : string ;
   note : Annotation [];
}
