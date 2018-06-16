import { Component, OnInit } from '@angular/core';

// AGREGADO PARA AUTENTICACION
import { AuthService } from './auth/auth.service';
// FIN AGREGADO

@Component({
  selector: 'app-callback',
  template: `
    <p>
      callback works!
    </p>
  `,
  styles: []
})
export class CallbackComponent implements OnInit {

// MODIFICADO PARA AUTENTICACION
  constructor(private authService: AuthService) { }
// FIN MODIFICADO

  ngOnInit() {
    this.authService.handleLoginCallback();
  }

}