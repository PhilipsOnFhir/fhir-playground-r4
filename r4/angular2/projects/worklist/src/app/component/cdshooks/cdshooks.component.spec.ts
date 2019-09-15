import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CdshooksComponent } from './cdshooks.component';

describe('CdshooksComponent', () => {
  let component: CdshooksComponent;
  let fixture: ComponentFixture<CdshooksComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CdshooksComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CdshooksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
