import { AbstractControl } from "@angular/forms";
import { GridApi } from "ag-grid-community";
import { Observable, startWith, identity, distinctUntilChanged, map, EMPTY } from "rxjs";
import { MaybeEmpty, Newable } from "./types";
import { inject } from "@angular/core";

export const isEmpty = (any: any) => any === null || any === undefined;

export const isNotEmpty = (any: any) => !isEmpty(any);

export const reloadBase = () => window.location.href = '/';

export const saveToLocalStorage = (key: string, value: any) => {
    localStorage.setItem(key, JSON.stringify(value));
};

export const getFromLocalStorage = (key: string) => {
    const value = localStorage.getItem(key);
    return value ? JSON.parse(value) : null;
};

export const empty2str = (input: string | undefined | null, defaultStr: string = ''): string => {
    if (typeof input === 'undefined' || input === null || !input) {
        return defaultStr;
    }
    return input.toString();
};

export const emptyIf = (input: any, emptyCondition: boolean): any => {
    if (emptyCondition) {
        return;
    }
    return input;
};

export const prefixIfStartsWithDot = (key: MaybeEmpty<string>, prefix: string) => {
    if (isNotEmpty(key)) {
        return key!.startsWith('.') ? prefix + key : key;
    }
    return '';
};

export const prefixIfNotEmpty = (key: MaybeEmpty<string>, prefix: string): string => {
    if (isNotEmpty(key)) {
        return prefix + key;
    }
    return '';
};

export const ctrlValue$ = <T>(control: AbstractControl, withoutDistinct = false): Observable<T> => control ? control.valueChanges.pipe(
    startWith(control.value),
    withoutDistinct ? identity : distinctUntilChanged(),
    map(() => control.value)) : EMPTY;

export const ctrlStatus$ = <T>(control: AbstractControl): Observable<string> => control.statusChanges.pipe(
    startWith(control.status),
    map(() => control.status)
);

export const createInstance = <T extends object>(type: Newable<T>): T => new type();

export const revive = <T extends Newable<any>>(objectCstr: T): T => Object.assign(createInstance(objectCstr), objectCstr);

export const assertType = <T>(value: T | null | undefined): T => value as T;

export const isCurrentDate = (something: Date): boolean => {
    const date = new Date(something);
    const currentDate = new Date();
    return date.getDate() === currentDate.getDate()
        && date.getMonth() === currentDate.getMonth()
        && date.getFullYear() === currentDate.getFullYear();
};

export const toBoolean = (value: MaybeEmpty<string>): boolean => isEmpty(value) ? false : value === 'true';

export const getFilteredRowData = <T>(gridApi: GridApi<T>): T[] => {
    const data: T[] = [];
    gridApi.forEachNodeAfterFilter((node) => {
        if (node.data) {
            data.push(node.data);
        }
    });
    return data;
};

export const formatFileSize = (bytes: number = 0, precision: number = 2, coef = 1000): string => {
    if (isNaN(parseFloat(String(bytes))) || !isFinite(bytes)) {
        return '?';
    }
    if (bytes === 0) {
        return '0 B';
    }
    const i = Math.floor(Math.log(bytes) / Math.log(coef));
    const units = ['bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
    return parseFloat((bytes / Math.pow(coef, i)).toFixed(precision)) + ' ' + units[i];
};

export const currencyFormatter = (currency: MaybeEmpty<string>, locale = 'en-EU') => new Intl.NumberFormat(locale, {
    style: 'currency',
    currency: empty2str(currency || 'EUR')
});

export const snakeToUpperCaseSeparated = (snakeCaseStr: string) => snakeCaseStr.replace('_', ' ').toUpperCase();

export const removeEmptyArrayProperties = <T extends object> (obj: T): T => {
    Object.keys(obj).forEach((key) => {
        const property = obj[key as keyof typeof obj];
        const propertyAsArr = property as Array<any>;
        if (Array.isArray(property) && !propertyAsArr.length) {
            delete obj[key as keyof typeof obj];
        } else {
            propertyAsArr.forEach((nestedProperty) => removeEmptyArrayProperties(nestedProperty));
        }
    });
    return obj;
}


