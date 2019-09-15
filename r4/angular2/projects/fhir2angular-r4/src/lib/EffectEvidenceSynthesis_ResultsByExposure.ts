import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { ExposureStateEnum } from './ExposureStateEnum'
import { Reference } from './Reference'

export class EffectEvidenceSynthesis_ResultsByExposure      extends BackboneElement
{

   static def : string = 'EffectEvidenceSynthesis_ResultsByExposure';
   description : string ;
   exposureState : ExposureStateEnum ;
   variantState : CodeableConcept ;
   riskEvidenceSynthesis : Reference ;
}
