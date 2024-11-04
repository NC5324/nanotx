import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckboxCellEditorComponent } from './checkbox-cell-editor.component';

describe('CheckboxCellEditorComponent', () => {
  let component: CheckboxCellEditorComponent;
  let fixture: ComponentFixture<CheckboxCellEditorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CheckboxCellEditorComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CheckboxCellEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
