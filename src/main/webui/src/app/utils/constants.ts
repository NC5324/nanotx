import { ColDef, GridOptions, SideBarDef, ToolPanelColumnCompParams } from 'ag-grid-community';
import { gridPersistenceEvents, loadTableState } from './ag-grid-persistence.utils';

export const API_BASE_URL = '/api';

export const STRIPE_API_BASE_URL = 'https://api.stripe.com/v1';

export const STRIPE_PUBLIC_KEY = 'STRIPE_PUBLIC_KEY';
export const STRIPE_PRIVATE_KEY = 'STRIPE_PRIVATE_KEY';

export const defaultSideBarDef: SideBarDef = {
    toolPanels: [
        'filters',
        {
            id: 'columns',
            labelDefault: 'Columns',
            labelKey: 'columns',
            iconKey: 'columns',
            toolPanel: 'agColumnsToolPanel',
            toolPanelParams: {
                suppressPivots: true,
                suppressPivotMode: true,
                suppressRowGroups: true,
                suppressValues: true,
            } as ToolPanelColumnCompParams,
        }
    ],
    hiddenByDefault: false,
    position: 'right',
}

export const defaultCommonColDef: ColDef = {
    floatingFilter: true,
    floatingFilterComponentParams: { suppressFilterButton: true },
    filter: 'agTextColumnFilter',
    resizable: true,
    sortable: true,
    menuTabs: [],
    minWidth: 150,
    initialFlex: 1,
}

export const leanDefaultColDef: ColDef = {
    filter: false,
    floatingFilter: false,
    suppressMovable: true,
    resizable: false,
    sortable: false,
    menuTabs: [],
}

export const leanGridOptions: GridOptions = {
    enableBrowserTooltips: true,
    enableCellTextSelection: true,
    suppressCellFocus: true,
    headerHeight: 40,  
}

export const defaultGridOptions: GridOptions = {
    ...gridPersistenceEvents,
    onFirstDataRendered: (params) => loadTableState(params.api, params.columnApi),
    enableBrowserTooltips: true,
    enableCellTextSelection: true,
    suppressContextMenu: true,
    suppressCellFocus: true,
    headerHeight: 40,
}

export const siderBarGridOptions: GridOptions = {
    ...defaultGridOptions,
    sideBar: defaultSideBarDef,
}

export const paginatedGridOptions: GridOptions = {
    ...defaultGridOptions,
    pagination: true,
    paginationAutoPageSize: true,
}