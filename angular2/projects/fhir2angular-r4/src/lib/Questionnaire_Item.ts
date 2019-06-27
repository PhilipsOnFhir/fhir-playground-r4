import { BackboneElement } from './BackboneElement'
import { Coding } from './Coding'
import { DomainResource } from './DomainResource'
import { EnableWhenBehaviorEnum } from './EnableWhenBehaviorEnum'
import { QuestionnaireItemTypeEnum } from './QuestionnaireItemTypeEnum'
import { Questionnaire_AnswerOption } from './Questionnaire_AnswerOption'
import { Questionnaire_EnableWhen } from './Questionnaire_EnableWhen'
import { Questionnaire_Initial } from './Questionnaire_Initial'

export class Questionnaire_Item      extends BackboneElement
{

   static def : string = 'Questionnaire_Item';
   linkId : string ;
   definition : string ;
   code : Coding [];
   prefix : string ;
   text : string ;
   type : QuestionnaireItemTypeEnum ;
   enableWhen : Questionnaire_EnableWhen [];
   enableBehavior : EnableWhenBehaviorEnum ;
   required : boolean ;
   repeats : boolean ;
   readOnly : boolean ;
   maxLength : string ;
   answerValueSet : string ;
   answerOption : Questionnaire_AnswerOption [];
   initial : Questionnaire_Initial [];
   item : Questionnaire_Item [];
}
