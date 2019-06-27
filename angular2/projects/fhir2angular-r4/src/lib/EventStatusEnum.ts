import { DomainResource } from './DomainResource';

export enum EventStatusEnum{
    COMPLETED = 'completed',
    ENTERED_IN_ERROR = 'entered-in-error',
    IN_PROGRESS = 'in-progress',
    NOT_DONE = 'not-done',
    ON_HOLD = 'on-hold',
    PREPARATION = 'preparation',
    STOPPED = 'stopped',
    UNKNOWN = 'unknown',
}
