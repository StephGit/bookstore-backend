import { Injectable } from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import {ReplaySubject} from 'rxjs/ReplaySubject';
import {HttpClient} from '@angular/common/http';
import {JwtService} from './jwt.service';
import {ApiService} from './api.service';
import {User} from '../models';

import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';


@Injectable()
export class UserService {

  private currentUserSubject = new BehaviorSubject<User>(new User());
  public currentUser = this.currentUserSubject.asObservable().distinctUntilChanged();

  private isAuthenticatedSubject = new ReplaySubject<boolean>(1);
  public isAuthenticated = this.isAuthenticatedSubject.asObservable();

  constructor (
    private apiService: ApiService,
    private http: HttpClient,
    private jwtService: JwtService
  ) {}

  // Verify JWT in localstorage with server & load user's info.
  // This runs once on application startup.
  populate() {
    const user = JSON.parse(localStorage.getItem('currentUser'));
    if (user) {
      this.setAuth(user);
    } else {
      this.purgeAuth();
    }
  }

  setAuth(user: User) {
    // Save JWT sent from server in localstorage
    this.jwtService.saveToken(user.token);
    // Set current user data into observable
    this.currentUserSubject.next(user);
    // Set isAuthenticated to true
    this.isAuthenticatedSubject.next(true);
  }

  purgeAuth() {
    // Remove JWT from localstorage
    this.jwtService.destroyToken();
    // Set current user to an empty object
    this.currentUserSubject.next(new User());
    // Set auth status to false
    this.isAuthenticatedSubject.next(false);
  }

  // attemptAuth(type, credentials): Observable<User> {
  //   const route = (type === 'login') ? '/login' : '';
  //   return this.apiService.post('/users' + route, {user: credentials})
  //     .map(
  //       data => {
  //         this.setAuth(data.user);
  //         return data;
  //       }
  //     );
  // }

  getCurrentUser(): User {
    return this.currentUserSubject.value;
  }
  //
  // // Update the user on the server (email, pass, etc)
  // update(user): Observable<User> {
  //   return this.apiService
  //     .put('/user', { user })
  //     .map(data => {
  //       // Update the currentUser observable
  //       this.currentUserSubject.next(data.user);
  //       return data.user;
  //     });
  // }
}
