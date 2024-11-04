import { AfterContentInit, ChangeDetectionStrategy, Component, EventEmitter, Input, OnChanges, Output, SimpleChange, SimpleChanges } from '@angular/core';
import { ColDef, GridOptions, GridReadyEvent, ICellRendererParams } from 'ag-grid-community';
import { BeFile } from '../../models/be-file.class';
import { defaultGridOptions, leanDefaultColDef } from '../../utils/constants';
import { formatFileSize } from '../../utils/utils';
import { LinkCellRendererComponent } from '../cell-renderers/link-cell-renderer/link-cell-renderer.component';
import { ButtonCellRendererComponent } from '../cell-renderers/button-cell-renderer/button-cell-renderer.component';
import { BaseComponent } from '../base/base.component';

@Component({
  selector: 'app-documents-table',
  templateUrl: './documents-table.component.html',
  styleUrls: ['./documents-table.component.sass'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DocumentsTableComponent extends BaseComponent {

  @Input() height = 200;
  @Input() documents: BeFile[] = [];
  
  @Output() documentRemove = new EventEmitter();
  @Output() gridReady = new EventEmitter();

  readonly gridOptions: GridOptions = {
    ...defaultGridOptions,
    getRowId: (params) => params.data?.id,
  };

  readonly defaultColDef: ColDef = {
    ...leanDefaultColDef,
    flex: 1
  };

  readonly columnDefs: ColDef<BeFile>[] = [
    {
      colId: 'fileName',
      headerName: 'File name',
      field: 'name',
      cellRendererSelector: (params: ICellRendererParams<BeFile>) => ({
        component: LinkCellRendererComponent,
        params: { link: params.data?.url }
      })
    },
    {
      colId: 'fileSize',
      headerName: 'File size',
      field: 'size',
      valueFormatter: (params) => formatFileSize(params.value),
    },
    {
      colId: 'uploadedBy',
      headerName: 'Uploaded by',
      field: 'creator',
    },
    {
      colId: 'action',
      headerName: 'Action',
      pinned: 'right',
      filter: false,
      sortable: false,
      maxWidth: 150,
      cellRenderer: ButtonCellRendererComponent,
      cellRendererParams: {
        buttons: [{
          id: 'bin',
          icon: 'pi pi-trash',
          onClick: (params: ICellRendererParams) => this.documentRemove.emit(params.data)
        }]
      },
    }
  ];

  constructor() {
    super();
  }

  onGridReady(event: GridReadyEvent): void {
    this.gridReady.emit(event);
  }

}
