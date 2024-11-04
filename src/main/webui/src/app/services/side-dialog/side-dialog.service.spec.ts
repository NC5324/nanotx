import { TestBed } from '@angular/core/testing';

import { SideDialogService } from './side-dialog.service';

describe('SideDialogService', () => {
  let service: SideDialogService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SideDialogService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
