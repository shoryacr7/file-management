import { Component, OnInit } from '@angular/core';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { FileService } from '../file.service';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-file-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './file-list.html',
  styleUrls: ['./file-list.css']
})
export class FileListComponent implements OnInit {
  fileInfos?: Observable<any>;
  fileListError: string | null = null;

  constructor(private fileService: FileService) { }

  ngOnInit(): void {
    this.loadFiles();
  }

  loadFiles(): void {
    this.fileInfos = this.fileService.getFiles().pipe(
      catchError(err => {
        this.fileListError = 'Unable to fetch list of files. Please try again later.';
        return of([]); // Return an empty array to clear the list on error
      })
    );
  }

  openFile(id: number): void {
    window.open(`http://localhost:8080/files/${id}`, '_blank');
  }
}
