import { DomainResource } from './DomainResource'
import { FinancialResourceStatusCodesEnum } from './FinancialResourceStatusCodesEnum'
import { Identifier } from './Identifier'
import { Reference } from './Reference'
import { VisionPrescription_LensSpecification } from './VisionPrescription_LensSpecification'

export class VisionPrescription      extends DomainResource
{

   static def : string = 'VisionPrescription';
   identifier : Identifier [];
   status : FinancialResourceStatusCodesEnum ;
   created : string ;
   patient : Reference ;
   encounter : Reference ;
   dateWritten : string ;
   prescriber : Reference ;
   lensSpecification : VisionPrescription_LensSpecification [];
}
