import { CodeableConcept } from './CodeableConcept'
import { Coding } from './Coding'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { MarketingStatus } from './MarketingStatus'
import { MedicinalProduct_ManufacturingBusinessOperation } from './MedicinalProduct_ManufacturingBusinessOperation'
import { MedicinalProduct_Name } from './MedicinalProduct_Name'
import { MedicinalProduct_SpecialDesignation } from './MedicinalProduct_SpecialDesignation'
import { Reference } from './Reference'

export class MedicinalProduct      extends DomainResource
{

   static def : string = 'MedicinalProduct';
   identifier : Identifier [];
   type : CodeableConcept ;
   domain : Coding ;
   combinedPharmaceuticalDoseForm : CodeableConcept ;
   legalStatusOfSupply : CodeableConcept ;
   additionalMonitoringIndicator : CodeableConcept ;
   specialMeasures : string [];
   paediatricUseIndicator : CodeableConcept ;
   productClassification : CodeableConcept [];
   marketingStatus : MarketingStatus [];
   pharmaceuticalProduct : Reference [];
   packagedMedicinalProduct : Reference [];
   attachedDocument : Reference [];
   masterFile : Reference [];
   contact : Reference [];
   clinicalTrial : Reference [];
   name : MedicinalProduct_Name [];
   crossReference : Identifier [];
   manufacturingBusinessOperation : MedicinalProduct_ManufacturingBusinessOperation [];
   specialDesignation : MedicinalProduct_SpecialDesignation [];
}
