import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PatientImageSelectorComponent } from './patient-image-selector.component';

describe('PatientImageSelectorComponent', () => {
  let component: PatientImageSelectorComponent;
  let fixture: ComponentFixture<PatientImageSelectorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PatientImageSelectorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PatientImageSelectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
