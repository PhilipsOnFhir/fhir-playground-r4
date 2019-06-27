import { DomainResource } from './DomainResource'
import { Element } from './Element'
import { ExpressionLanguageEnum } from './ExpressionLanguageEnum'

export class Expression      extends Element
{

   static def : string = 'Expression';
   description : string ;
   name : string ;
   language : ExpressionLanguageEnum ;
   expression : string ;
   reference : string ;
}
