import { DomainResource } from './DomainResource';

export enum RequestStatusEnum{
    ACTIVE = 'active',
    COMPLETED = 'completed',
    DRAFT = 'draft',
    ENTERED_IN_ERROR = 'entered-in-error',
    ON_HOLD = 'on-hold',
    REVOKED = 'revoked',
    UNKNOWN = 'unknown',
}
