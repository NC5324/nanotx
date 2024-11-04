import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';

interface LetContext<T> {
  appLet: T | null;
}

/**
 * Allows for evaluating a template expression and storing its result in template variable.
 */
@Directive({
  selector: '[appLet]'
})
export class LetDirective<T> {

  private context: LetContext<T> = { appLet: null };

  constructor(private viewContainer: ViewContainerRef, private templateRef: TemplateRef<LetContext<T>>) {
    viewContainer.createEmbeddedView(templateRef, this.context);
  }

  @Input()
  set appLet(value: T) {
    this.context.appLet = value;
  }

}
