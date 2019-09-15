import { BackboneElement } from './BackboneElement'
import { DomainResource } from './DomainResource'
import { OperationDefinition_Binding } from './OperationDefinition_Binding'
import { OperationDefinition_ReferencedFrom } from './OperationDefinition_ReferencedFrom'
import { OperationParameterUseEnum } from './OperationParameterUseEnum'
import { SearchParamTypeEnum } from './SearchParamTypeEnum'

export class OperationDefinition_Parameter      extends BackboneElement
{

   static def : string = 'OperationDefinition_Parameter';
   name : string ;
   use : OperationParameterUseEnum ;
   min : string ;
   max : string ;
   documentation : string ;
   type : string ;
   targetProfile : string [];
   searchType : SearchParamTypeEnum ;
   binding : OperationDefinition_Binding ;
   referencedFrom : OperationDefinition_ReferencedFrom [];
   part : OperationDefinition_Parameter [];
}
