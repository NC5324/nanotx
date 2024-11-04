import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PartOfferDialogComponent } from './part-offer-dialog.component';

describe('PartOfferDialogComponent', () => {
  let component: PartOfferDialogComponent;
  let fixture: ComponentFixture<PartOfferDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PartOfferDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PartOfferDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
