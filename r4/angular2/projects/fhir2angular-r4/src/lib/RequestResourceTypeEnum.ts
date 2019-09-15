import { DomainResource } from './DomainResource';

export enum RequestResourceTypeEnum{
    APPOINTMENT = 'Appointment',
    APPOINTMENTRESPONSE = 'AppointmentResponse',
    CAREPLAN = 'CarePlan',
    CLAIM = 'Claim',
    COMMUNICATIONREQUEST = 'CommunicationRequest',
    CONTRACT = 'Contract',
    DEVICEREQUEST = 'DeviceRequest',
    ENROLLMENTREQUEST = 'EnrollmentRequest',
    IMMUNIZATIONRECOMMENDATION = 'ImmunizationRecommendation',
    MEDICATIONREQUEST = 'MedicationRequest',
    NUTRITIONORDER = 'NutritionOrder',
    SERVICEREQUEST = 'ServiceRequest',
    SUPPLYREQUEST = 'SupplyRequest',
    TASK = 'Task',
    VISIONPRESCRIPTION = 'VisionPrescription',
}
