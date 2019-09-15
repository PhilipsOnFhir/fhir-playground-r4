import { AssertionDirectionTypeEnum } from './AssertionDirectionTypeEnum'
import { AssertionOperatorTypeEnum } from './AssertionOperatorTypeEnum'
import { AssertionResponseTypesEnum } from './AssertionResponseTypesEnum'
import { BackboneElement } from './BackboneElement'
import { DomainResource } from './DomainResource'
import { TestScriptRequestMethodCodeEnum } from './TestScriptRequestMethodCodeEnum'

export class TestScript_Assert      extends BackboneElement
{

   static def : string = 'TestScript_Assert';
   label : string ;
   description : string ;
   direction : AssertionDirectionTypeEnum ;
   compareToSourceId : string ;
   compareToSourceExpression : string ;
   compareToSourcePath : string ;
   contentType : string ;
   expression : string ;
   headerField : string ;
   minimumId : string ;
   navigationLinks : boolean ;
   operator : AssertionOperatorTypeEnum ;
   path : string ;
   requestMethod : TestScriptRequestMethodCodeEnum ;
   requestURL : string ;
   resource : string ;
   response : AssertionResponseTypesEnum ;
   responseCode : string ;
   sourceId : string ;
   validateProfileId : string ;
   value : string ;
   warningOnly : boolean ;
}
