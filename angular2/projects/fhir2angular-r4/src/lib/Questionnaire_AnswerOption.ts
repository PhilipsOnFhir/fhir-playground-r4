import { BackboneElement } from './BackboneElement'
import { Coding } from './Coding'
import { DomainResource } from './DomainResource'
import { Reference } from './Reference'

export class Questionnaire_AnswerOption      extends BackboneElement
{

   static def : string = 'Questionnaire_AnswerOption';
   valueInteger : string ;
   valueDate : string ;
   valueTime : string ;
   valueString : string ;
   valueCoding : Coding ;
   valueReference : Reference ;
   initialSelected : boolean ;
}
