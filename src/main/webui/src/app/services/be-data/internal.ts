import { map, Observable } from 'rxjs';
import { Serializable, SerializableCstr } from '../../models/serializable.abstract';

/**
 * Decorator for reviving the result of observable functions
 * @param mapper a reviver function
 */
export const revive = (mapper: (resp: any) => any) => (target: any, propertyKey: string, descriptor: any) => {
  const declaredFn = descriptor.value;
  descriptor.value = function(...args: any[]): Observable<any> {
    const result = declaredFn.apply(this, args);
    return result.pipe(map((r) => mapper(r)));
  };
};

/**
 * Revives dumb JS elements as class instances
 */
export const as = <TCstr extends SerializableCstr<T>, T extends Serializable>(entityCstr: TCstr): (resp: any | any[]) => typeof resp => {
  return (resp: any | any[]) => {
    if (Array.isArray(resp)) {
      return resp.map((item) => entityCstr.revive(item));
    }
    return entityCstr.revive(resp);
  }
};