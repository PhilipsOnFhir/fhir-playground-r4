import { BackboneElement } from './BackboneElement'
import { ConstraintSeverityEnum } from './ConstraintSeverityEnum'
import { DomainResource } from './DomainResource'

export class ElementDefinition_Constraint      extends BackboneElement
{

   static def : string = 'ElementDefinition_Constraint';
   key : string ;
   requirements : string ;
   severity : ConstraintSeverityEnum ;
   human : string ;
   expression : string ;
   xpath : string ;
   source : string ;
}
