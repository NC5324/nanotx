import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InputCellEditorComponent } from './input-cell-editor.component';

describe('InputCellEditorComponent', () => {
  let component: InputCellEditorComponent;
  let fixture: ComponentFixture<InputCellEditorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InputCellEditorComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InputCellEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
