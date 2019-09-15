import { BackboneElement } from './BackboneElement'
import { Coding } from './Coding'
import { DomainResource } from './DomainResource'
import { TestScriptRequestMethodCodeEnum } from './TestScriptRequestMethodCodeEnum'
import { TestScript_RequestHeader } from './TestScript_RequestHeader'

export class TestScript_Operation      extends BackboneElement
{

   static def : string = 'TestScript_Operation';
   type : Coding ;
   resource : string ;
   label : string ;
   description : string ;
   accept : string ;
   contentType : string ;
   destination : string ;
   encodeRequestUrl : boolean ;
   method : TestScriptRequestMethodCodeEnum ;
   origin : string ;
   params : string ;
   requestHeader : TestScript_RequestHeader [];
   requestId : string ;
   responseId : string ;
   sourceId : string ;
   targetId : string ;
   url : string ;
}
