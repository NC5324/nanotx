import { ProviderToken, InjectOptions } from "@angular/core";

export type Newable<T> = new (...args: any[]) => T;

export type MaybeArray<T> = T | T[];

export type MaybeEmpty<T> = T | undefined | null;

export type PropertyType<TObj, TProp extends keyof TObj> = TObj[TProp];

export type IfEquals<X, Y, A, B> = (<T>() => T extends X ? 1 : 2) extends
    (<T>() => T extends Y ? 1 : 2) ? A : B;

export type WritableKeyOf<T> = { [P in keyof T]: IfEquals<{ [Q in P]: T[P] }, { -readonly [Q in P]: T[P] }, P, never> }[keyof T];

export type Transient<T> = T;