import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { ICellRendererAngularComp } from 'ag-grid-angular';
import { BehaviorSubject, Observable, isObservable } from 'rxjs';

@Component({
  selector: 'app-tag-cell-renderer',
  templateUrl: './tag-cell-renderer.component.html',
  styleUrls: ['./tag-cell-renderer.component.sass'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TagCellRendererComponent implements OnInit, ICellRendererAngularComp {

  readonly disabledCache$ = new Map<string, BehaviorSubject<boolean>>();

  params!: any;

  agInit(params: any): void {
    this.params = params;
  }

  refresh(params: any): boolean {
    return false;
  }

  ngOnInit(): void {
  }

}
