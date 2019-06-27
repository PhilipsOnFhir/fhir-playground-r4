import { TestBed } from '@angular/core/testing';

import { Fhir2angularR4Service } from './fhir2angular-r4.service';

describe('Fhir2angularR4Service', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: Fhir2angularR4Service = TestBed.get(Fhir2angularR4Service);
    expect(service).toBeTruthy();
  });
});
