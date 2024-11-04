import { DatePipe } from '@angular/common';
import { Injectable } from '@angular/core';
import { ColDef, ExcelCell, ExcelExportParams, ExcelStyle, GridOptions, ValueFormatterParams } from 'ag-grid-community';

@Injectable({
  providedIn: 'root'
})
export class AgGridExcelExportOptionsService {

  private exportTitle = '';

  private defaultExcelExportParams: ExcelExportParams = {
    author: 'NanoTX',
    headerRowHeight: 60,
    sheetName: 'export'
  };

  constructor(private readonly datePipe: DatePipe) {
  }

  private get excelStyles(): ExcelStyle[] {
    return [
      {
        id: 'blueHeaderCell',
        interior: {
          color: '#25586e', pattern: 'Solid'
        },
        font: {
          color: '#ffffff',
          size: 20
        },
        alignment: {
          vertical: 'Center'
        }
      },
      {
        id: 'blueTextHeader',
        font: {
          color: '#25586e', size: 16
        }
      },
      {
        id: 'excelDefaultHeader',
        interior: {
          color: '#25586e', pattern: 'Solid'
        },
        font: {
          color: '#ffffff'
        },
        alignment: {
          horizontal: 'Center',
          vertical: 'Center'
        },
        borders: {
          borderLeft: {
            color: '#ffffff',
            lineStyle: 'Continuous',
            weight: 2
          },
          borderRight: {
            color: '#ffffff',
            lineStyle: 'Continuous',
            weight: 2
          }
        }
      }
    ];
  }

  initDefaultExcelExportParams(gridOptions: GridOptions, exportName: string, exportTitle: string): void {
    this.exportTitle = exportTitle;
    const defaultExcelExportParams = Object.assign({}, this.defaultExcelExportParams);
    defaultExcelExportParams.processCellCallback = (params) => {
      const colDef = params.column.getColDef();
      // try to reuse valueFormatter from the colDef
      if (typeof colDef.valueFormatter === 'function') {
        const valueFormatterParams: ValueFormatterParams = {
          ...params,
          data: params.node?.data,
          node: params.node || null,
          colDef: params.column.getColDef()
        };
        return colDef.valueFormatter(valueFormatterParams);
      }
      return params.value;
    };
    defaultExcelExportParams.prependContent = [{
      cells: this.generateFirstRowHeaderCells(gridOptions.columnDefs ?
         gridOptions.columnDefs.filter((col) => {
          if(col as ColDef){
            return !(col as ColDef).hide;
          }
            return true;
         }).length : 0),
      height: 50
    }, {
      cells: [{
        data: { value: exportName, type: 'String' },
        styleId: 'blueTextHeader',
        mergeAcross: 1
      },{
        data: { value: this.datePipe.transform(new Date(), 'dd.MM.YYYY'), type: 'String' },
      }],
      height: 50
    }];
    if (gridOptions.excelStyles && gridOptions.excelStyles.length) {
      gridOptions.excelStyles = gridOptions.excelStyles.concat(this.excelStyles);
    } else {
      gridOptions.excelStyles = this.excelStyles;
    }
    if (gridOptions.defaultColDef) {
      if (gridOptions.defaultColDef.headerClass) {
        if (typeof gridOptions.defaultColDef.headerClass === 'string') {
          gridOptions.defaultColDef.headerClass += 'excelDefaultHeader';
        }
        if (typeof gridOptions.defaultColDef.headerClass === 'object') {
          gridOptions.defaultColDef.headerClass.push('excelDefaultHeader');
        }
      }
      else {
        gridOptions.defaultColDef.headerClass = 'excelDefaultHeader';
      }
    }
    gridOptions.defaultExcelExportParams = defaultExcelExportParams;
  }

  private generateFirstRowHeaderCells(colNumber: number): ExcelCell[] {
    const defaultCell: ExcelCell = {
      styleId: 'blueHeaderCell'
    };
    const arr = [];
    for (let i = 0; i < colNumber; i++) {
      arr.push({ ...defaultCell });
    }
    arr[1].data = {
      value: this.exportTitle,
      type: 'String'
    };
    return arr;
  }
}
