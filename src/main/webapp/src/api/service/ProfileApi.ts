import api from "../Api";
import { IAccount, profileStore } from "../../stores/ProfileStore";
import { organisationHeader } from "../../config/AppConfig";

export const getProfile = async () => {
  const result = await api.get<IAccount>("/account");

  if (result.status === 200 && result.data) {
    api.setHeader(organisationHeader, result.data.employee.organisation);
    profileStore.setProfile(result.data);
  }

  return result;
};
