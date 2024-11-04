import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LinkCellRendererComponent } from './link-cell-renderer.component';

describe('LinkCellRendererComponent', () => {
  let component: LinkCellRendererComponent;
  let fixture: ComponentFixture<LinkCellRendererComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LinkCellRendererComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LinkCellRendererComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
