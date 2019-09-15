import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'

export class DeviceDefinition_Material      extends BackboneElement
{

   static def : string = 'DeviceDefinition_Material';
   substance : CodeableConcept ;
   alternate : boolean ;
   allergenicIndicator : boolean ;
}
