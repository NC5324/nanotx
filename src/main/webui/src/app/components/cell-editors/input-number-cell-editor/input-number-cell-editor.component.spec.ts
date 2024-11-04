import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InputNumberCellEditorComponent } from './input-number-cell-editor.component';

describe('InputNumberCellEditorComponent', () => {
  let component: InputNumberCellEditorComponent;
  let fixture: ComponentFixture<InputNumberCellEditorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InputNumberCellEditorComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InputNumberCellEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
