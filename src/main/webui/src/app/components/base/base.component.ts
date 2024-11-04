import { ChangeDetectionStrategy, Component, OnChanges, OnDestroy, SimpleChange } from '@angular/core';
import { BehaviorSubject, Observable, Subject, filter, map } from 'rxjs';

export interface TypedSimpleChange<T> {
  previousValue: T;
  currentValue: T;
}

@Component({
  selector: 'app-base',
  templateUrl: './base.component.html',
  styleUrls: ['./base.component.sass'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class BaseComponent implements OnChanges, OnDestroy {
  private readonly changes$ = new BehaviorSubject<{ [key: string]: SimpleChange }>({});
  protected readonly destroyed$ = new Subject<void>();

  public observeChange$<T = any>(propertyName: string): Observable<TypedSimpleChange<T>> {
    return this.changes$.pipe(
      filter(changes => changes.hasOwnProperty(propertyName)),
      map(changes => changes[propertyName])
    );
  }

  public observe$<T = any>(propertyName: string): Observable<T> {
    return this.observeChange$<T>(propertyName).pipe(map(change => change.currentValue));
  }

  ngOnChanges(changes: { [key: string]: SimpleChange }) {
    this.changes$.next(changes);
  }

  ngOnDestroy(): void {
    this.destroyed$.next();
    this.destroyed$.complete();
  }
}
