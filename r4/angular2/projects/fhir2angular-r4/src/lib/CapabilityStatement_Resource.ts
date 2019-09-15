import { BackboneElement } from './BackboneElement'
import { CapabilityStatement_Interaction } from './CapabilityStatement_Interaction'
import { CapabilityStatement_Operation } from './CapabilityStatement_Operation'
import { CapabilityStatement_SearchParam } from './CapabilityStatement_SearchParam'
import { ConditionalDeleteStatusEnum } from './ConditionalDeleteStatusEnum'
import { ConditionalReadStatusEnum } from './ConditionalReadStatusEnum'
import { DomainResource } from './DomainResource'
import { ReferenceHandlingPolicyEnum } from './ReferenceHandlingPolicyEnum'
import { ResourceVersionPolicyEnum } from './ResourceVersionPolicyEnum'

export class CapabilityStatement_Resource      extends BackboneElement
{

   static def : string = 'CapabilityStatement_Resource';
   type : string ;
   profile : string ;
   supportedProfile : string [];
   documentation : string ;
   interaction : CapabilityStatement_Interaction [];
   versioning : ResourceVersionPolicyEnum ;
   readHistory : boolean ;
   updateCreate : boolean ;
   conditionalCreate : boolean ;
   conditionalRead : ConditionalReadStatusEnum ;
   conditionalUpdate : boolean ;
   conditionalDelete : ConditionalDeleteStatusEnum ;
   referencePolicy : ReferenceHandlingPolicyEnum [];
   searchInclude : string [];
   searchRevInclude : string [];
   searchParam : CapabilityStatement_SearchParam [];
   operation : CapabilityStatement_Operation [];
}
