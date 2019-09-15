import { TestBed } from '@angular/core/testing';

import { SmartonfhirR4Service } from './smartonfhir-r4.service';

describe('SmartonfhirR4Service', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: SmartonfhirR4Service = TestBed.get(SmartonfhirR4Service);
    expect(service).toBeTruthy();
  });
});
