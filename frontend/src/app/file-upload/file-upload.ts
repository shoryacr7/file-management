import { Component, ViewChild, ElementRef } from '@angular/core';
import { HttpEventType, HttpResponse } from '@angular/common/http';
import { FileService } from '../file.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-file-upload',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './file-upload.html',
  styleUrls: ['./file-upload.css']
})
export class FileUploadComponent {
  @ViewChild('fileInput') fileInput!: ElementRef;
  currentFile?: File;
  message = '';

  constructor(private fileService: FileService) { }

  selectFile(event: any): void {
    this.message = '';
    const file = event.target.files[0];
    if (file) {
      const allowedTypes = ['text/plain', 'image/jpeg', 'image/png', 'application/json'];
      if (allowedTypes.includes(file.type)) {
        this.currentFile = file;
      } else {
        this.currentFile = undefined;
        this.message = 'Invalid file type. Please select a .txt, .jpg, .png, or .json file.';
      }
    }
  
  }

  reset(): void {
    this.currentFile = undefined;
    this.message = '';
    this.fileInput.nativeElement.value = '';
  }

  upload(): void {
    if (this.currentFile) {
      this.fileService.upload(this.currentFile).subscribe(
        (event: any) => {
          if (event.type === HttpEventType.UploadProgress) {
            // Not used
          } else if (event instanceof HttpResponse) {
            this.message = event.body.message;
            this.currentFile = undefined;
            this.fileInput.nativeElement.value = '';
          }
        },
        (err: any) => {
          console.log(err);
          this.message = 'Could not upload the file!';
        });
    }
  }
}
