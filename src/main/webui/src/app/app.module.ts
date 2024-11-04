import { AsyncPipe, DatePipe } from '@angular/common';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { MenubarModule } from 'primeng/menubar';
import { PasswordModule } from 'primeng/password';
import { SplitButtonModule } from 'primeng/splitbutton';
import { TableModule } from 'primeng/table';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BaseComponent } from './components/base/base.component';
import { LetDirective } from './directives/let/let.directive';
import { AuthInterceptor } from './http-interceptors/auth.interceptor';
import { PartRequestOverviewComponent } from './pages/part-request-overview/part-request-overview.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { SigninComponent } from './pages/signin/signin.component';
import { SignupComponent } from './pages/signup/signup.component';
import { EditorModule } from 'primeng/editor';
import { PanelModule } from 'primeng/panel';
import { InputNumberModule } from 'primeng/inputnumber';
import { DropdownModule } from 'primeng/dropdown';
import { ToastModule } from 'primeng/toast';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { ButtonCellRendererComponent } from './components/cell-renderers/button-cell-renderer/button-cell-renderer.component';
import { InputCellEditorComponent } from './components/cell-editors/input-cell-editor/input-cell-editor.component';
import { InputNumberCellEditorComponent } from './components/cell-editors/input-number-cell-editor/input-number-cell-editor.component';
import { CheckboxCellEditorComponent } from './components/cell-editors/checkbox-cell-editor/checkbox-cell-editor.component';
import { AgGridModule } from 'ag-grid-angular';
import { AppGridDirective } from './directives/app-grid/app-grid.directive';
import { ExcelExportModule } from 'ag-grid-enterprise';
import { ModuleRegistry, CsvExportModule } from 'ag-grid-community';
import { SideDialogComponent } from './components/side-dialog/side-dialog.component';
import { PartRequestDialogComponent } from './dialogs/part-request-dialog/part-request-dialog.component';
import { DEFAULT_DIALOG_CONFIG, DialogModule } from '@angular/cdk/dialog';
import { HomeComponent } from './pages/home/home.component';
import { LayoutMainComponent } from './pages/layout-main/layout-main.component';
import { AboutComponent } from './pages/about/about.component';
import { PartOfferCommentsComponent } from './components/part-offer-comments/part-offer-comments.component';
import { PartOfferDialogComponent } from './dialogs/part-offer-dialog/part-offer-dialog.component';
import { SideMenuComponent } from './components/side-menu/side-menu.component';
import { AngularFullpageModule } from '@fullpage/angular-fullpage';
import { AccordionModule } from 'primeng/accordion';
import { TabViewModule } from 'primeng/tabview';
import { TagModule } from 'primeng/tag';
import { ChipModule } from 'primeng/chip';
import { RadioButtonModule } from 'primeng/radiobutton';
import { TagCellRendererComponent } from './components/cell-renderers/tag-cell-renderer/tag-cell-renderer.component';
import { DocumentsTableComponent } from './components/documents-table/documents-table.component';
import { LinkCellRendererComponent } from './components/cell-renderers/link-cell-renderer/link-cell-renderer.component';
import { FileUploadModule } from 'primeng/fileupload';
import { DataViewModule } from 'primeng/dataview';
import { InputSwitchModule } from 'primeng/inputswitch';
import { ToggleButtonModule } from 'primeng/togglebutton';
import { ToolbarModule } from 'primeng/toolbar';
import { PartOfferCommentDialogComponent } from './dialogs/part-offer-comment-dialog/part-offer-comment-dialog.component';
import { DialogService, DynamicDialogModule } from 'primeng/dynamicdialog';
import { PartOfferPaymentDialogComponent } from './dialogs/part-offer-payment-dialog/part-offer-payment-dialog.component';
import { NgxStripeModule } from 'ngx-stripe';
import { STRIPE_PUBLIC_KEY } from './utils/constants';
import { PartOfferOrderConfirmationDialogComponent } from './dialogs/part-offer-order-confirmation-dialog/part-offer-order-confirmation-dialog.component';
import { CheckboxModule } from 'primeng/checkbox';


ModuleRegistry.registerModules([
  ExcelExportModule,
  CsvExportModule,
]);

@NgModule({
  declarations: [
    AppComponent,
    PartRequestOverviewComponent,
    BaseComponent,
    SignupComponent,
    SigninComponent,
    ProfileComponent,
    LetDirective,
    ButtonCellRendererComponent,
    InputCellEditorComponent,
    InputNumberCellEditorComponent,
    CheckboxCellEditorComponent,
    AppGridDirective,
    SideDialogComponent,
    PartRequestDialogComponent,
    HomeComponent,
    LayoutMainComponent,
    AboutComponent,
    PartOfferCommentsComponent,
    PartOfferDialogComponent,
    SideMenuComponent,
    TagCellRendererComponent,
    DocumentsTableComponent,
    LinkCellRendererComponent,
    PartOfferCommentDialogComponent,
    PartOfferPaymentDialogComponent,
    PartOfferOrderConfirmationDialogComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    TableModule,
    MenubarModule,
    ButtonModule,
    SplitButtonModule,
    InputTextModule,
    PasswordModule,
    ButtonModule,
    EditorModule,
    PanelModule,
    InputNumberModule,
    DropdownModule,
    ToastModule,
    AgGridModule,
    DialogModule,
    AutoCompleteModule,
    DropdownModule,
    AngularFullpageModule,
    AccordionModule,
    TabViewModule,
    ChipModule,
    TagModule,
    FileUploadModule,
    DataViewModule,
    InputSwitchModule,
    ToggleButtonModule,
    ToolbarModule,
    RadioButtonModule,
    CheckboxModule,
    DynamicDialogModule,
    NgxStripeModule.forRoot(STRIPE_PUBLIC_KEY),
  ],
  providers: [
    AsyncPipe,
    DatePipe,
    DialogService,
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    { provide: DEFAULT_DIALOG_CONFIG, useValue: { hasBackdrop: true }}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
