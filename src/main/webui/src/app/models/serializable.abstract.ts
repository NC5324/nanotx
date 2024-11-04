import { FormGroup } from "@angular/forms";
import { Newable, MaybeArray } from "../utils/types";
import { isEmpty } from "../utils/utils";

export interface SerializableCstr<T extends Serializable> {
    revive(data: T): T;
}

const isSerializableCstr = (cstr: any): cstr is SerializableCstr<any> => cstr.revive && typeof cstr.revive === 'function';

export abstract class Serializable {
  
  protected form?: FormGroup;

  /**
   * Revives a dumb JS object as a class instance
   */
  static revive<TCstr extends SerializableCstr<T> & Newable<T>, T extends Serializable>(
    this: TCstr,
    data: T
  ): T {
    if (data instanceof this || !data) {
      return data;
    }
    const instance = new this();
    Object.assign(instance, { ...data, tsTemplate: new this() });
    instance.reviveProperties();
    return instance;
  }

  /**
   * Revives nested dumb JS object as a class instance
   */
  protected reviveProperty<TCstr extends Newable<any>>(typeCstr: TCstr, keys: MaybeArray<string>): void {
    if (!Array.isArray(keys)) {
      keys = [keys];
    }

    const reviveFn: (data: any) => InstanceType<TCstr> = isSerializableCstr(typeCstr)
      ? (data: any) => typeCstr.revive(data)
      : (data: any) => new typeCstr(data);

    keys.forEach((key: string) => {
      const prop = (this as any)[key];
      if (Array.isArray(prop)) {
        prop.forEach((propArrItm, idx) => {
          if (propArrItm && !(propArrItm instanceof typeCstr)) {
            prop[idx] = reviveFn(propArrItm);
          }
        });
      } else if (prop && !(prop instanceof typeCstr)) {
        (this as any)[key] = reviveFn(prop);
      } else if (isEmpty(prop) && (this as any).tsTemplate[key]) {
        (this as any)[key] = (this as any).tsTemplate[key];
      }
    });
  }

  protected getForm(): FormGroup {
    if (!this.form) {
      this.form = new FormGroup({});
    }
    return this.form;
  }

  public getDataToSave(): any {
    return {};
  }

  protected abstract reviveProperties(): void;
}