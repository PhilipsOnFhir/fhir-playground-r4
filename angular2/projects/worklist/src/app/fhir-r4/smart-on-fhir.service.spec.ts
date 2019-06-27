import { TestBed } from '@angular/core/testing';

import { SmartOnFhirService } from './smart-on-fhir.service';

describe('SmartOnFhirService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: SmartOnFhirService = TestBed.get(SmartOnFhirService);
    expect(service).toBeTruthy();
  });
});
