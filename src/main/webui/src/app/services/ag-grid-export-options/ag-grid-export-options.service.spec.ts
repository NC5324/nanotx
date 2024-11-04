import { TestBed } from '@angular/core/testing';

import { AgGridExportOptionsService } from './ag-grid-export-options.service';

describe('AgGridExportOptionsService', () => {
  let service: AgGridExportOptionsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AgGridExportOptionsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
