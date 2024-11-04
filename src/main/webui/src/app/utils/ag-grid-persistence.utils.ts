import { ColumnState, GridApi, ColumnApi, SortChangedEvent, FilterChangedEvent, ColumnMovedEvent, ColumnResizedEvent, ColumnPinnedEvent, ColumnVisibleEvent } from "ag-grid-community";
import { getFromLocalStorage, saveToLocalStorage } from "./utils";
import { debounce } from "lodash-es";

interface TableState {
    columns?: ColumnState[];
    filterModel?: { [key: string]: any };
};

export const loadTableState = (gridApi: GridApi, columnApi: ColumnApi): TableState => {
    const loadState = getFromLocalStorage('tableState') as TableState || {};
    if(loadState.columns){
        columnApi.applyColumnState({
            state: loadState.columns,
            applyOrder: true
        });
    }
    if(loadState.filterModel){
        gridApi.setFilterModel(loadState.filterModel);
    }
    return loadState;
};

export const saveTableState = (gridApi: GridApi, columnApi: ColumnApi) => {
    const columns = columnApi.getColumnState();
    if (!columns || columns.length === 0) {
        return;
    }
    const colStates: ColumnState[] = columns.map(
        (col) => ({
            colId: col.colId,
            width: col.width,
            sort: col.sort,
            hide: col.hide,
        } as ColumnState)
    );
    saveToLocalStorage('tableState', {
        columns: colStates,
        filterModel: gridApi.getFilterModel()
    });
};

export const debouncedSaveTableState = debounce(saveTableState, 1000);

export const gridPersistenceEvents = {
    onSortChanged: (params: SortChangedEvent) => debouncedSaveTableState(params.api, params.columnApi),
    onFilterChanged: (params: FilterChangedEvent) => debouncedSaveTableState(params.api, params.columnApi),
    onColumnMoved: (params: ColumnMovedEvent) => debouncedSaveTableState(params.api, params.columnApi),
    onColumnResized: (params: ColumnResizedEvent) => debouncedSaveTableState(params.api, params.columnApi),
    onColumnPinned: (params: ColumnPinnedEvent) => debouncedSaveTableState(params.api, params.columnApi),
    onColumnVisible: (params: ColumnVisibleEvent) => debouncedSaveTableState(params.api, params.columnApi),
};