import { Routes } from '@angular/router';
import { FileUploadComponent } from './file-upload/file-upload';
import { FileListComponent } from './file-list/file-list';

export const routes: Routes = [
  { path: '', redirectTo: 'upload', pathMatch: 'full' },
  { path: 'upload', component: FileUploadComponent },
  { path: 'list', component: FileListComponent },
];
