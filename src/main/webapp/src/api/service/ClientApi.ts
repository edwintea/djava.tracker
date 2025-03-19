import api from "../Api";
import {
  clientStore,
  IClient,
  IClientEmployeeCount,
  IClientMinimal,
} from "../../stores/ClientStore";
import { uiStore } from "../../stores/UIStore";

export const createClient = async (data: IClient) => {
  const result = await api.post<IClient>("/clients", data);

  if (result.status === 201 && result.data) {
    uiStore.setSnackbarShow(true, "Created client successfully!", "success");
  }
};

export const getClientsPaged = async (page: number, size: number) => {
  const result = await api.get<IClientEmployeeCount[]>(
    `/clients/specific?page=${page}&size=${size}`
  );

  if (result.status === 200 && result.data) {
    clientStore.setClients(result.data);
    return result;
  }
  return null;
};

export const getClientMinimal = async () => {
  const result = await api.get<IClientMinimal[]>("/clients/minimal/specific");

  if (result.status === 200 && result.data) {
    clientStore.setClientMinimal(result.data);
  }
};
