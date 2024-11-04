import { TestBed } from '@angular/core/testing';

import { PartRequestResolver } from './part-request.resolver';

describe('PartRequestResolver', () => {
  let resolver: PartRequestResolver;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    resolver = TestBed.inject(PartRequestResolver);
  });

  it('should be created', () => {
    expect(resolver).toBeTruthy();
  });
});
