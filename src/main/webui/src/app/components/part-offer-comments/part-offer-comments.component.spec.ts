import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PartOfferCommentsComponent } from './part-offer-comments.component';

describe('PartOfferCommentsComponent', () => {
  let component: PartOfferCommentsComponent;
  let fixture: ComponentFixture<PartOfferCommentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PartOfferCommentsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PartOfferCommentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
