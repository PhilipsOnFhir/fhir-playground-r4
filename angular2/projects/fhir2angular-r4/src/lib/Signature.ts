import { Coding } from './Coding'
import { DomainResource } from './DomainResource'
import { Element } from './Element'
import { Reference } from './Reference'

export class Signature      extends Element
{

   static def : string = 'Signature';
   type : Coding [];
   when : string ;
   who : Reference ;
   onBehalfOf : Reference ;
   targetFormat : string ;
   sigFormat : string ;
   data : string ;
}
