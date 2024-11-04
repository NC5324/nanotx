import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { ICellRendererAngularComp } from 'ag-grid-angular';
import { ICellRendererParams } from 'ag-grid-community';
import { Observable, BehaviorSubject, isObservable } from 'rxjs';

interface IButtonCellRendererParams<TData = any, TValue = any> extends ICellRendererParams<TData, TValue> {
  buttons: IButtonCellRendererModel<TData, TValue>[];
}

interface IButtonCellRendererModel<TData = any, TValue = any> {
  id: string;
  icon: string;
  styleClass?: string;
  disabled: boolean | Observable<boolean> | ((params: ICellRendererParams<TData, TValue>) => boolean);
  size?: 'sm' | 'md';
  onClick?: (params: IButtonCellRendererParams<TData, TValue>) => any;
}

@Component({
  selector: 'app-button-cell-renderer',
  templateUrl: './button-cell-renderer.component.html',
  styleUrls: ['./button-cell-renderer.component.sass'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ButtonCellRendererComponent implements OnInit, ICellRendererAngularComp {

  readonly disabledCache$ = new Map<string, BehaviorSubject<boolean>>();

  params!: IButtonCellRendererParams;

  disabled$(button: IButtonCellRendererModel): Observable<boolean> {
    if (isObservable(button.disabled)) {
      return button.disabled;
    }
    const disabled = typeof button.disabled === 'function' ? button.disabled(this.params) : button.disabled;
    if (!this.disabledCache$.has(button.id)) {
      this.disabledCache$.set(button.id, new BehaviorSubject<boolean>(disabled));
    } else {
      const disabled$ = this.disabledCache$.get(button.id);
      disabled$?.next(disabled);
    }
    return this.disabledCache$.get(button.id) as Observable<boolean>;
  }

  agInit(params: IButtonCellRendererParams): void {
    this.params = params;
  }

  refresh(params: IButtonCellRendererParams): boolean {
    return true;
  }

  ngOnInit(): void {
  }

  onClick(button: IButtonCellRendererModel): void {
    if (button.onClick) {
      button.onClick(this.params);
    }
  }

}
