import { CodeableConcept } from './CodeableConcept'
import { ContactDetail } from './ContactDetail'
import { DomainResource } from './DomainResource'
import { PublicationStatusEnum } from './PublicationStatusEnum'
import { SearchComparatorEnum } from './SearchComparatorEnum'
import { SearchModifierCodeEnum } from './SearchModifierCodeEnum'
import { SearchParamTypeEnum } from './SearchParamTypeEnum'
import { SearchParameter_Component } from './SearchParameter_Component'
import { UsageContext } from './UsageContext'
import { XPathUsageTypeEnum } from './XPathUsageTypeEnum'

export class SearchParameter      extends DomainResource
{

   static def : string = 'SearchParameter';
   url : string ;
   version : string ;
   name : string ;
   derivedFrom : string ;
   status : PublicationStatusEnum ;
   experimental : boolean ;
   date : string ;
   publisher : string ;
   contact : ContactDetail [];
   description : string ;
   useContext : UsageContext [];
   jurisdiction : CodeableConcept [];
   purpose : string ;
   code : string ;
   base : string [];
   type : SearchParamTypeEnum ;
   expression : string ;
   xpath : string ;
   xpathUsage : XPathUsageTypeEnum ;
   target : string [];
   multipleOr : boolean ;
   multipleAnd : boolean ;
   comparator : SearchComparatorEnum [];
   modifier : SearchModifierCodeEnum [];
   chain : string [];
   component : SearchParameter_Component [];
}
