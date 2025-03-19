import { makeAutoObservable } from "mobx";

class AuthenticationStore {
  constructor() {
    makeAutoObservable(this);
  }

  isAuthenticated: boolean | null = null;
  setIsAuthenticated = (isAuthenticated: boolean) => {
    this.isAuthenticated = isAuthenticated;
  };
}

export const authenticationStore = new AuthenticationStore();
