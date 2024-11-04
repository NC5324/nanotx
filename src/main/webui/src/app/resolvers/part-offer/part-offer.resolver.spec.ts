import { TestBed } from '@angular/core/testing';

import { PartOfferResolver } from './part-offer.resolver';

describe('PartOfferResolver', () => {
  let resolver: PartOfferResolver;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    resolver = TestBed.inject(PartOfferResolver);
  });

  it('should be created', () => {
    expect(resolver).toBeTruthy();
  });
});
