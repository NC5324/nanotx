import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PartRequestDialogComponent } from './part-request-dialog.component';

describe('PartRequestDialogComponent', () => {
  let component: PartRequestDialogComponent;
  let fixture: ComponentFixture<PartRequestDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PartRequestDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PartRequestDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
