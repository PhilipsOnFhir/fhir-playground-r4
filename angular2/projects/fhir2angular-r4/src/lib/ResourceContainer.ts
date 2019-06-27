import { Account } from './Account'
import { ActivityDefinition } from './ActivityDefinition'
import { AdverseEvent } from './AdverseEvent'
import { AllergyIntolerance } from './AllergyIntolerance'
import { Appointment } from './Appointment'
import { AppointmentResponse } from './AppointmentResponse'
import { AuditEvent } from './AuditEvent'
import { Basic } from './Basic'
import { Binary } from './Binary'
import { BiologicallyDerivedProduct } from './BiologicallyDerivedProduct'
import { BodyStructure } from './BodyStructure'
import { Bundle } from './Bundle'
import { CapabilityStatement } from './CapabilityStatement'
import { CarePlan } from './CarePlan'
import { CareTeam } from './CareTeam'
import { CatalogEntry } from './CatalogEntry'
import { ChargeItem } from './ChargeItem'
import { ChargeItemDefinition } from './ChargeItemDefinition'
import { Claim } from './Claim'
import { ClaimResponse } from './ClaimResponse'
import { ClinicalImpression } from './ClinicalImpression'
import { CodeSystem } from './CodeSystem'
import { Communication } from './Communication'
import { CommunicationRequest } from './CommunicationRequest'
import { CompartmentDefinition } from './CompartmentDefinition'
import { Composition } from './Composition'
import { ConceptMap } from './ConceptMap'
import { Condition } from './Condition'
import { Consent } from './Consent'
import { Contract } from './Contract'
import { Coverage } from './Coverage'
import { CoverageEligibilityRequest } from './CoverageEligibilityRequest'
import { CoverageEligibilityResponse } from './CoverageEligibilityResponse'
import { DetectedIssue } from './DetectedIssue'
import { Device } from './Device'
import { DeviceDefinition } from './DeviceDefinition'
import { DeviceMetric } from './DeviceMetric'
import { DeviceRequest } from './DeviceRequest'
import { DeviceUseStatement } from './DeviceUseStatement'
import { DiagnosticReport } from './DiagnosticReport'
import { DocumentManifest } from './DocumentManifest'
import { DocumentReference } from './DocumentReference'
import { DomainResource } from './DomainResource'
import { EffectEvidenceSynthesis } from './EffectEvidenceSynthesis'
import { Encounter } from './Encounter'
import { Endpoint } from './Endpoint'
import { EnrollmentRequest } from './EnrollmentRequest'
import { EnrollmentResponse } from './EnrollmentResponse'
import { EpisodeOfCare } from './EpisodeOfCare'
import { EventDefinition } from './EventDefinition'
import { Evidence } from './Evidence'
import { EvidenceVariable } from './EvidenceVariable'
import { ExampleScenario } from './ExampleScenario'
import { ExplanationOfBenefit } from './ExplanationOfBenefit'
import { FamilyMemberHistory } from './FamilyMemberHistory'
import { Flag } from './Flag'
import { Goal } from './Goal'
import { GraphDefinition } from './GraphDefinition'
import { Group } from './Group'
import { GuidanceResponse } from './GuidanceResponse'
import { HealthcareService } from './HealthcareService'
import { ImagingStudy } from './ImagingStudy'
import { Immunization } from './Immunization'
import { ImmunizationEvaluation } from './ImmunizationEvaluation'
import { ImmunizationRecommendation } from './ImmunizationRecommendation'
import { ImplementationGuide } from './ImplementationGuide'
import { InsurancePlan } from './InsurancePlan'
import { Invoice } from './Invoice'
import { Library } from './Library'
import { Linkage } from './Linkage'
import { List } from './List'
import { Location } from './Location'
import { Measure } from './Measure'
import { MeasureReport } from './MeasureReport'
import { Media } from './Media'
import { Medication } from './Medication'
import { MedicationAdministration } from './MedicationAdministration'
import { MedicationDispense } from './MedicationDispense'
import { MedicationKnowledge } from './MedicationKnowledge'
import { MedicationRequest } from './MedicationRequest'
import { MedicationStatement } from './MedicationStatement'
import { MedicinalProduct } from './MedicinalProduct'
import { MedicinalProductAuthorization } from './MedicinalProductAuthorization'
import { MedicinalProductContraindication } from './MedicinalProductContraindication'
import { MedicinalProductIndication } from './MedicinalProductIndication'
import { MedicinalProductIngredient } from './MedicinalProductIngredient'
import { MedicinalProductInteraction } from './MedicinalProductInteraction'
import { MedicinalProductManufactured } from './MedicinalProductManufactured'
import { MedicinalProductPackaged } from './MedicinalProductPackaged'
import { MedicinalProductPharmaceutical } from './MedicinalProductPharmaceutical'
import { MedicinalProductUndesirableEffect } from './MedicinalProductUndesirableEffect'
import { MessageDefinition } from './MessageDefinition'
import { MessageHeader } from './MessageHeader'
import { MolecularSequence } from './MolecularSequence'
import { NamingSystem } from './NamingSystem'
import { NutritionOrder } from './NutritionOrder'
import { Observation } from './Observation'
import { ObservationDefinition } from './ObservationDefinition'
import { OperationDefinition } from './OperationDefinition'
import { OperationOutcome } from './OperationOutcome'
import { Organization } from './Organization'
import { OrganizationAffiliation } from './OrganizationAffiliation'
import { Parameters } from './Parameters'
import { Patient } from './Patient'
import { PaymentNotice } from './PaymentNotice'
import { PaymentReconciliation } from './PaymentReconciliation'
import { Person } from './Person'
import { PlanDefinition } from './PlanDefinition'
import { Practitioner } from './Practitioner'
import { PractitionerRole } from './PractitionerRole'
import { Procedure } from './Procedure'
import { Provenance } from './Provenance'
import { Questionnaire } from './Questionnaire'
import { QuestionnaireResponse } from './QuestionnaireResponse'
import { RelatedPerson } from './RelatedPerson'
import { RequestGroup } from './RequestGroup'
import { ResearchDefinition } from './ResearchDefinition'
import { ResearchElementDefinition } from './ResearchElementDefinition'
import { ResearchStudy } from './ResearchStudy'
import { ResearchSubject } from './ResearchSubject'
import { RiskAssessment } from './RiskAssessment'
import { RiskEvidenceSynthesis } from './RiskEvidenceSynthesis'
import { Schedule } from './Schedule'
import { SearchParameter } from './SearchParameter'
import { ServiceRequest } from './ServiceRequest'
import { Slot } from './Slot'
import { Specimen } from './Specimen'
import { SpecimenDefinition } from './SpecimenDefinition'
import { StructureDefinition } from './StructureDefinition'
import { StructureMap } from './StructureMap'
import { Subscription } from './Subscription'
import { Substance } from './Substance'
import { SubstanceNucleicAcid } from './SubstanceNucleicAcid'
import { SubstancePolymer } from './SubstancePolymer'
import { SubstanceProtein } from './SubstanceProtein'
import { SubstanceReferenceInformation } from './SubstanceReferenceInformation'
import { SubstanceSourceMaterial } from './SubstanceSourceMaterial'
import { SubstanceSpecification } from './SubstanceSpecification'
import { SupplyDelivery } from './SupplyDelivery'
import { SupplyRequest } from './SupplyRequest'
import { Task } from './Task'
import { TerminologyCapabilities } from './TerminologyCapabilities'
import { TestReport } from './TestReport'
import { TestScript } from './TestScript'
import { ValueSet } from './ValueSet'
import { VerificationResult } from './VerificationResult'
import { VisionPrescription } from './VisionPrescription'

export class ResourceContainer{

   static def : string = 'ResourceContainer';
   Account : Account ;
   ActivityDefinition : ActivityDefinition ;
   AdverseEvent : AdverseEvent ;
   AllergyIntolerance : AllergyIntolerance ;
   Appointment : Appointment ;
   AppointmentResponse : AppointmentResponse ;
   AuditEvent : AuditEvent ;
   Basic : Basic ;
   Binary : Binary ;
   BiologicallyDerivedProduct : BiologicallyDerivedProduct ;
   BodyStructure : BodyStructure ;
   Bundle : Bundle ;
   CapabilityStatement : CapabilityStatement ;
   CarePlan : CarePlan ;
   CareTeam : CareTeam ;
   CatalogEntry : CatalogEntry ;
   ChargeItem : ChargeItem ;
   ChargeItemDefinition : ChargeItemDefinition ;
   Claim : Claim ;
   ClaimResponse : ClaimResponse ;
   ClinicalImpression : ClinicalImpression ;
   CodeSystem : CodeSystem ;
   Communication : Communication ;
   CommunicationRequest : CommunicationRequest ;
   CompartmentDefinition : CompartmentDefinition ;
   Composition : Composition ;
   ConceptMap : ConceptMap ;
   Condition : Condition ;
   Consent : Consent ;
   Contract : Contract ;
   Coverage : Coverage ;
   CoverageEligibilityRequest : CoverageEligibilityRequest ;
   CoverageEligibilityResponse : CoverageEligibilityResponse ;
   DetectedIssue : DetectedIssue ;
   Device : Device ;
   DeviceDefinition : DeviceDefinition ;
   DeviceMetric : DeviceMetric ;
   DeviceRequest : DeviceRequest ;
   DeviceUseStatement : DeviceUseStatement ;
   DiagnosticReport : DiagnosticReport ;
   DocumentManifest : DocumentManifest ;
   DocumentReference : DocumentReference ;
   EffectEvidenceSynthesis : EffectEvidenceSynthesis ;
   Encounter : Encounter ;
   Endpoint : Endpoint ;
   EnrollmentRequest : EnrollmentRequest ;
   EnrollmentResponse : EnrollmentResponse ;
   EpisodeOfCare : EpisodeOfCare ;
   EventDefinition : EventDefinition ;
   Evidence : Evidence ;
   EvidenceVariable : EvidenceVariable ;
   ExampleScenario : ExampleScenario ;
   ExplanationOfBenefit : ExplanationOfBenefit ;
   FamilyMemberHistory : FamilyMemberHistory ;
   Flag : Flag ;
   Goal : Goal ;
   GraphDefinition : GraphDefinition ;
   Group : Group ;
   GuidanceResponse : GuidanceResponse ;
   HealthcareService : HealthcareService ;
   ImagingStudy : ImagingStudy ;
   Immunization : Immunization ;
   ImmunizationEvaluation : ImmunizationEvaluation ;
   ImmunizationRecommendation : ImmunizationRecommendation ;
   ImplementationGuide : ImplementationGuide ;
   InsurancePlan : InsurancePlan ;
   Invoice : Invoice ;
   Library : Library ;
   Linkage : Linkage ;
   List : List ;
   Location : Location ;
   Measure : Measure ;
   MeasureReport : MeasureReport ;
   Media : Media ;
   Medication : Medication ;
   MedicationAdministration : MedicationAdministration ;
   MedicationDispense : MedicationDispense ;
   MedicationKnowledge : MedicationKnowledge ;
   MedicationRequest : MedicationRequest ;
   MedicationStatement : MedicationStatement ;
   MedicinalProduct : MedicinalProduct ;
   MedicinalProductAuthorization : MedicinalProductAuthorization ;
   MedicinalProductContraindication : MedicinalProductContraindication ;
   MedicinalProductIndication : MedicinalProductIndication ;
   MedicinalProductIngredient : MedicinalProductIngredient ;
   MedicinalProductInteraction : MedicinalProductInteraction ;
   MedicinalProductManufactured : MedicinalProductManufactured ;
   MedicinalProductPackaged : MedicinalProductPackaged ;
   MedicinalProductPharmaceutical : MedicinalProductPharmaceutical ;
   MedicinalProductUndesirableEffect : MedicinalProductUndesirableEffect ;
   MessageDefinition : MessageDefinition ;
   MessageHeader : MessageHeader ;
   MolecularSequence : MolecularSequence ;
   NamingSystem : NamingSystem ;
   NutritionOrder : NutritionOrder ;
   Observation : Observation ;
   ObservationDefinition : ObservationDefinition ;
   OperationDefinition : OperationDefinition ;
   OperationOutcome : OperationOutcome ;
   Organization : Organization ;
   OrganizationAffiliation : OrganizationAffiliation ;
   Patient : Patient ;
   PaymentNotice : PaymentNotice ;
   PaymentReconciliation : PaymentReconciliation ;
   Person : Person ;
   PlanDefinition : PlanDefinition ;
   Practitioner : Practitioner ;
   PractitionerRole : PractitionerRole ;
   Procedure : Procedure ;
   Provenance : Provenance ;
   Questionnaire : Questionnaire ;
   QuestionnaireResponse : QuestionnaireResponse ;
   RelatedPerson : RelatedPerson ;
   RequestGroup : RequestGroup ;
   ResearchDefinition : ResearchDefinition ;
   ResearchElementDefinition : ResearchElementDefinition ;
   ResearchStudy : ResearchStudy ;
   ResearchSubject : ResearchSubject ;
   RiskAssessment : RiskAssessment ;
   RiskEvidenceSynthesis : RiskEvidenceSynthesis ;
   Schedule : Schedule ;
   SearchParameter : SearchParameter ;
   ServiceRequest : ServiceRequest ;
   Slot : Slot ;
   Specimen : Specimen ;
   SpecimenDefinition : SpecimenDefinition ;
   StructureDefinition : StructureDefinition ;
   StructureMap : StructureMap ;
   Subscription : Subscription ;
   Substance : Substance ;
   SubstanceNucleicAcid : SubstanceNucleicAcid ;
   SubstancePolymer : SubstancePolymer ;
   SubstanceProtein : SubstanceProtein ;
   SubstanceReferenceInformation : SubstanceReferenceInformation ;
   SubstanceSourceMaterial : SubstanceSourceMaterial ;
   SubstanceSpecification : SubstanceSpecification ;
   SupplyDelivery : SupplyDelivery ;
   SupplyRequest : SupplyRequest ;
   Task : Task ;
   TerminologyCapabilities : TerminologyCapabilities ;
   TestReport : TestReport ;
   TestScript : TestScript ;
   ValueSet : ValueSet ;
   VerificationResult : VerificationResult ;
   VisionPrescription : VisionPrescription ;
   Parameters : Parameters ;
}
