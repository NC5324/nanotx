import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable, Injector, inject } from "@angular/core";
import { EMPTY, Observable, catchError, throwError } from "rxjs";
import { AuthService } from "../services/auth/auth.service";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    constructor(private readonly injector: Injector) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(req).pipe(
            catchError((err) => {
                if (this.isUnauthorizedError(req, err)) {
                    this.injector.get(AuthService).signOut();
                    return EMPTY;
                }
                return throwError(() => new Error(err));
            })
        );
    }

    isUnauthorizedError(req: HttpRequest<any>, error: any): boolean {
        return !req.url.includes('stripe') && error instanceof HttpErrorResponse && error.status === 401;
    }
}