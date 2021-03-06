import { Component, OnInit } from '@angular/core';
import {ToastrService} from 'ngx-toastr';
import {GenericService} from '../../services/generic.service';
import {LogsComponent} from '../logs/logs.component';

@Component({
  selector: 'app-db-logs',
  templateUrl: '../logs/logs.component.html',
  styleUrls: ['../logs/logs.component.css']
})
export class DbLogsComponent extends LogsComponent implements OnInit {

  constructor(protected toast: ToastrService, protected genericService: GenericService) {
    super(toast, genericService);
  }

  ngOnInit() {
    super.initialize('Regex', 'filter');
  }

}
