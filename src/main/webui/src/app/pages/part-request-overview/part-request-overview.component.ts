import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AppStateService } from 'src/app/services/app-state/app-state.service';
import { BeDataService } from 'src/app/services/be-data/be-data.service';
import { BaseComponent } from '../../components/base/base.component';
import { AuthService } from 'src/app/services/auth/auth.service';
import { ColDef, GridOptions, RowDoubleClickedEvent, GridReadyEvent, GridApi, ColumnApi, ValueFormatterParams, ICellRendererParams } from 'ag-grid-community';
import { defaultCommonColDef, paginatedGridOptions, siderBarGridOptions } from 'src/app/utils/constants';
import { AgGridExcelExportOptionsService } from 'src/app/services/ag-grid-export-options/ag-grid-export-options.service';
import { DatePipe } from '@angular/common';
import { PartRequest, PartRequestState } from 'src/app/models/part-request.class';
import { currencyFormatter } from 'src/app/utils/utils';
import { PartOffer, PartOfferState } from 'src/app/models/part-offer.class';
import { Observable, filter, map, shareReplay, takeUntil, combineLatest, take } from 'rxjs';
import { SideDialogService } from 'src/app/services/side-dialog/side-dialog.service';
import { PartRequestDialogComponent } from 'src/app/dialogs/part-request-dialog/part-request-dialog.component';
import { ButtonCellRendererComponent } from 'src/app/components/cell-renderers/button-cell-renderer/button-cell-renderer.component';
import { PartOfferDialogComponent } from 'src/app/dialogs/part-offer-dialog/part-offer-dialog.component';
import { TagCellRendererComponent } from 'src/app/components/cell-renderers/tag-cell-renderer/tag-cell-renderer.component';
import { User } from 'src/app/models/user.class';
import { defaultGridOptions } from '../../utils/constants';

export type AgGridRow = PartRequest | PartOffer;

@Component({
  selector: 'app-part-request-overview',
  templateUrl: './part-request-overview.component.html',
  styleUrls: ['./part-request-overview.component.sass'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PartRequestOverviewComponent extends BaseComponent implements OnInit {

  readonly gridOptions: GridOptions = {
    ...paginatedGridOptions,
    sideBar: true,
    treeData: true,
    getDataPath: (data) => data.requestId ? [String(data.requestId), String(data.id)] : [String(data.id)],
    getRowId: (params) => `${params.data instanceof PartRequest ? 'REQUEST' : 'OFFER'} - ${params.data.id}`,
    suppressLoadingOverlay: true, // TODO: Customize
    groupDefaultExpanded: 1,
    autoGroupColumnDef: {
      headerName: 'ID',
      cellRendererParams: {
        suppressCount: true,
      },
      pinned: 'left',
      sort: 'desc',
    },
    rowClassRules: {
      'ntx-darker-background': (params) => params.data instanceof PartRequest,
      'font-medium': (params) => params.data instanceof PartRequest,
      'ntx-default-background': (params) => params.data instanceof PartOffer,
    },
  };

  readonly defaultColDef: ColDef = {
    ...defaultCommonColDef,

  }

  readonly colDefs$: Observable<ColDef<AgGridRow>[]> = this.auth.currentUser$.pipe(
    filter((user) => !!user),
    map((user) => user as User),
    map<User, Array<ColDef<AgGridRow>>>((currentUser) => [
      {
        colId: 'componentName',
        headerName: 'Component name',
        field: 'componentName',
      },
      {
        colId: 'componentType',
        headerName: 'Component type',
        field: 'componentType',
        pivot: true,
      },
      {
        colId: 'category',
        headerName: 'Category',
        field: 'category',
      },
      {
        colId: 'manufacturer',
        headerName: 'Manufacturer',
        field: 'manufacturer',
      },
      {
        colId: 'partNumber',
        headerName: 'Part number',
        field: 'partNumber',
      },
      {
        colId: 'alternatePartNumber',
        headerName: 'Alternative part number',
        field: 'alternatePartNumber',
      },
      {
        colId: 'quantity',
        headerName: 'Quantity',
        field: 'quantity',
        filter: 'agNumberColumnFilter',
      },
      {
        colId: 'price',
        headerName: 'Price / Piece',
        field: 'targetPrice',
        filter: 'agNumberColumnFilter',
        valueFormatter: (params) => currencyFormatter(params.data?.currency || 'EUR').format(params.value),
      },
      {
        colId: 'minTemp',
        headerName: 'Min. Temp °C',
        field: 'minTemp',
        filter: 'agNumberColumnFilter',
      },
      {
        colId: 'maxTemp',
        headerName: 'Max. Temp °C',
        field: 'maxTemp',
        filter: 'agNumberColumnFilter',
      },
      {
        colId: 'state',
        headerName: 'State',
        pinned: 'right',
        field: 'state',
        maxWidth: 200,
        cellRendererSelector: (params) => ({
          component: TagCellRendererComponent,
          params: {
            label: params.data?.stateLabel,
            background: params.data?.stateBackground,
          }
        }),
      },
      {
        colId: 'action',
        headerName: 'Action',
        pinned: 'right',
        filter: false,
        maxWidth: 200,
        cellRendererSelector: (params) => this.getActionCellRenderer(params, currentUser),
      }
    ]),
    shareReplay(1),
  );

  readonly rowData$: Observable<AgGridRow[]> = this.appState.partRequests$.pipe(
    map((partRequests) => partRequests.flatMap((request) => [request, ...request.offers])),
    shareReplay(1),
  );

  private gridApi!: GridApi;
  private columnApi!: ColumnApi;

  constructor(
    readonly router: Router,
    readonly auth: AuthService,
    readonly appState: AppStateService,
    private readonly beData: BeDataService,
    private readonly exportOptions: AgGridExcelExportOptionsService,
    private readonly datePipe: DatePipe,
    private readonly sideDialog: SideDialogService,
    private readonly route: ActivatedRoute,
  ) {
    super();
  }

  ngOnInit(): void {
    combineLatest([this.route.url, this.rowData$]).pipe(take(1)).subscribe(([url, rowData]) => {
      const path = url[0].path;
      const id = Number(url[1].path);
      if (!isNaN(id)) {
        if (path === 'request') {
          const request = rowData.find((row) => row.id === id && row instanceof PartRequest);
          if (request) {
            this.openDetailedView({ data: request } as ICellRendererParams);
          }
        } else if (path === 'offer') {
          const offer = rowData.find((row) => row.id === id && row instanceof PartOffer);
          if (offer) {
            this.openDetailedView({ data: offer} as ICellRendererParams);
          }
        }
        this.router.navigate(['/']);
      }
    });
  }

  onGridReady(event: GridReadyEvent) {
    this.gridApi = event.api;
    this.columnApi = event.columnApi;
    this.exportOptions.initDefaultExcelExportParams(this.gridOptions, 'NanoTX - Part requests', 'Part requests');
  }

  getActionCellRenderer(params: ICellRendererParams, user: User): any {
    if (params.data) {
      return {
        component: ButtonCellRendererComponent,
        params: {
          buttons: this.getActionButtons(params, user),
        }
      };
    }
  }

  getActionButtons(params: ICellRendererParams, user: User): any {
    const [edit, remove, duplicate, addOffer] = [
      {
        id: 'edit',
        icon: 'pi pi-eye',
        onClick: () => this.openDetailedView(params),
      },
      {
        id: 'remove',
        icon: 'pi pi-trash',
        onClick: () => this.deleteRow(params),
      },
      {
        id: 'duplicate',
        icon: 'pi pi-copy',
        onClick: () => this.duplicateRow(params),
      },
      {
        id: 'addOffer',
        icon: 'pi pi-plus',
        onClick: () => this.openCreateOfferView(params),
      },
    ];
    if (user.isAdmin) {
      if (params.data instanceof PartRequest) {
        return [edit, duplicate, remove, addOffer];
      }
      return [edit, duplicate, remove];
    }
    const buttons = [edit, duplicate];
    if (params.data instanceof PartRequest) {
      if ((params.data.state === PartRequestState.OPEN || !params.data.offers.some((offer) => offer.state === PartOfferState.ORDER_CONFIRMED)) && user.isBuyer) {
        buttons.push(remove);
      }
      if (params.data.state === PartRequestState.IN_PROGRESS && user.isSupplier) {
        buttons.push(addOffer);
      }
    }
    if (params.data instanceof PartOffer) {
      if (params.data.state === PartOfferState.OPEN || params.data.state === PartOfferState.WITHDRAWN) {
        if (user.isSupplier) {
          buttons.push(remove);
        }
      }
    }
    return buttons;
  }

  openDetailedView(params: ICellRendererParams): void {
    if (params.data instanceof PartRequest) {
      this.beData.loadPartRequest$(params.data.id).pipe(takeUntil(this.destroyed$)).subscribe((request) => {
        this.appState.partRequestContext$.next(request);
        this.sideDialog.open(PartRequestDialogComponent, {
          data: { gridApi: this.gridApi }
        });
      });
    } else if (params.data instanceof PartOffer) {
      combineLatest([
        this.beData.loadPartRequest$(params.data.requestId).pipe(takeUntil(this.destroyed$)),
        this.beData.loadPartOffer$(params.data.id).pipe(takeUntil(this.destroyed$)),
      ]).pipe(takeUntil(this.destroyed$)).subscribe(([request, offer]) => {
        this.appState.partRequestContext$.next(request);
        this.appState.partOfferContext$.next(offer);
        this.sideDialog.open(PartOfferDialogComponent, {
          data: { gridApi: this.gridApi } 
        });
      });
    }
  }

  openCreateRequestView(): void {
    this.appState.partRequestContext$.next(new PartRequest());
    this.sideDialog.open(PartRequestDialogComponent);
  }

  openCreateOfferView(params: ICellRendererParams): void {
    this.appState.partRequestContext$.next(params.data);
    this.appState.partOfferContext$.next(new PartOffer());
    this.sideDialog.open(PartOfferDialogComponent);
  }

  duplicateRow(params: ICellRendererParams): void {
    console.log(params.data);
    const createNewInstance = params.data instanceof PartRequest ? () => new PartOffer() : () => new PartRequest();
    const newInstance = Object.assign(createNewInstance(), params.data);
    const dataToSave = newInstance.getDataToSave();
    dataToSave.state = params.data instanceof PartRequest ? PartRequestState.OPEN : PartOfferState.OPEN;
    dataToSave.requestId = params.data.requestId;
    delete dataToSave.id;
    delete dataToSave.rootId;
    const save$: Observable<PartRequest | PartOffer> = params.data instanceof PartRequest
      ? this.beData.savePartRequest$(dataToSave)
      : this.beData.savePartOffer$(dataToSave);
    save$.pipe(takeUntil(this.destroyed$)).subscribe((duplicatedEntry) => {
      this.appState.reloadRequests$.next();
    });
  }

  deleteRow(params: ICellRendererParams): void {
    const dataToSave = params.data.getDataToSave();
    dataToSave.deleted = true;
    const save$: Observable<PartRequest | PartOffer> = params.data instanceof PartRequest
      ? this.beData.savePartRequest$(dataToSave)
      : this.beData.savePartOffer$(dataToSave);
    save$.pipe(takeUntil(this.destroyed$)).subscribe((deletedRequest) => {
      this.appState.reloadRequests$.next();
    });
  }
  
  clearFilters(): void {
    this.gridApi.setFilterModel(null);
  }

  collapseAll(): void {
    this.gridApi.collapseAll();
  }

  expandAll(): void {
    this.gridApi.expandAll();
  }

  exportExcel(): void {
    this.gridApi.exportDataAsExcel({
      fileName: `NanoTX-Part-requests-${this.datePipe.transform(new Date(), 'dd.MM.YYYY')}.xlsx`
    });
  }

}
