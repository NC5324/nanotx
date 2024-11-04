import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { ICellRendererAngularComp } from 'ag-grid-angular';
import { ICellRendererParams } from 'ag-grid-community';

interface ILinkCellRendererParams<T = any, V = any> extends ICellRendererParams<T, V> {
  link: string;
}

@Component({
  selector: 'app-link-cell-renderer',
  templateUrl: './link-cell-renderer.component.html',
  styleUrls: ['./link-cell-renderer.component.sass'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class LinkCellRendererComponent implements OnInit, ICellRendererAngularComp {

  params!: ILinkCellRendererParams;

  constructor() { }

  agInit(params: ILinkCellRendererParams<any, any>): void {
    this.params = params;
  }

  refresh(params: ILinkCellRendererParams<any, any>): boolean {
    return false;
  }

  ngOnInit(): void {
  }

}
