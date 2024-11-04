import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PartOfferCommentDialogComponent } from './part-offer-comment-dialog.component';

describe('PartOfferCommentDialogComponent', () => {
  let component: PartOfferCommentDialogComponent;
  let fixture: ComponentFixture<PartOfferCommentDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PartOfferCommentDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PartOfferCommentDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
