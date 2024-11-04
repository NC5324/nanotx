import { Observable, filter, map } from "rxjs";
import { MaybeEmpty } from "./types";

export function ensureTruthy() {
  return function<T>(source: Observable<MaybeEmpty<T>>): Observable<T> {
    return source.pipe(
      filter((value) => !!value),
      map((value) => value as T)
    );
  }
}


export function reverse() {
  return function<T>(source: Observable<boolean>): Observable<boolean> {
    return source.pipe(
      map((value) => !value)
    );
  }
}