import { AggregationModeEnum } from './AggregationModeEnum'
import { BackboneElement } from './BackboneElement'
import { DomainResource } from './DomainResource'
import { ReferenceVersionRulesEnum } from './ReferenceVersionRulesEnum'

export class ElementDefinition_Type      extends BackboneElement
{

   static def : string = 'ElementDefinition_Type';
   code : string ;
   profile : string [];
   targetProfile : string [];
   aggregation : AggregationModeEnum [];
   versioning : ReferenceVersionRulesEnum ;
}
