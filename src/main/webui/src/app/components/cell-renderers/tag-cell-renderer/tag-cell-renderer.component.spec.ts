import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TagCellRendererComponent } from './tag-cell-renderer.component';

describe('TagCellRendererComponent', () => {
  let component: TagCellRendererComponent;
  let fixture: ComponentFixture<TagCellRendererComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TagCellRendererComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TagCellRendererComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
