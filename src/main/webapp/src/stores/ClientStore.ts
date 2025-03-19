import { makeAutoObservable } from "mobx";

export interface IClient {
  id?: string;
  name: string;
  sinceDate: string;
  address: string;
  organisation: string;
}

export interface IClientEmployeeCount extends IClient {
  employeeCount: number;
}

export interface IClientMinimal {
  id: string;
  name: string;
}

class ClientStore {
  constructor() {
    makeAutoObservable(this);
  }

  clients: IClientEmployeeCount[] = [];
  setClients = (clients: IClientEmployeeCount[]) => {
    this.clients = clients;
  };

  clientMinimal: IClientMinimal[] = [];
  setClientMinimal = (clientMinimal: IClientMinimal[]) => {
    this.clientMinimal = clientMinimal;
  };
}

export const clientStore = new ClientStore();
