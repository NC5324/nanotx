import { TestBed } from '@angular/core/testing';

import { BeDataService } from './be-data.service';

describe('BeDataService', () => {
  let service: BeDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BeDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
