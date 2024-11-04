import { Directive } from '@angular/core';
import { AgGridAngular } from 'ag-grid-angular';
import { GridColumnsChangedEvent, RowDataUpdatedEvent, ColDef, ColGroupDef } from 'ag-grid-community';

@Directive({
  selector: '[appGrid]'
})
export class AppGridDirective {

  constructor(private host: AgGridAngular) { }

  translateHeaders(event: GridColumnsChangedEvent | RowDataUpdatedEvent): void {
    const colDefs: (ColDef | ColGroupDef)[] = event.api.getColumnDefs() || [];
    colDefs.forEach((col) => {
      if (!col.headerTooltip) {
        col.headerTooltip = col.headerName;
      }
      const colDef = col as ColDef;
      if (!colDef.tooltipField && !colDef.tooltipValueGetter) {
        colDef.tooltipValueGetter = (params) => params.valueFormatted || params.value;
      }
    });
    if (colDefs.length) {
      event.api.setColumnDefs(colDefs);
    }
  }

}
