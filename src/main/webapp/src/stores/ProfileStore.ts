import { makeAutoObservable } from "mobx";
import { defaultIEmployee, IEmployee } from "./EmployeeStore";

export interface IAccount {
  user: IProfile;
  employee: IEmployee;
}

export interface IProfile {
  activated: boolean;
  authorities: string[];
  createdBy: string;
  createdDate: string;
  email: string;
  firstName: string;
  id: string;
  lastModifiedBy: string;
  lastModifiedDate: string;
  lastName: string;
  login: string;
}

const defaultIProfile: IProfile = {
  activated: false,
  authorities: [],
  createdBy: "",
  createdDate: "",
  email: "",
  firstName: "",
  id: "",
  lastModifiedBy: "",
  lastModifiedDate: "",
  lastName: "",
  login: "",
};

const defaultIAccount: IAccount = {
  employee: defaultIEmployee,
  user: defaultIProfile,
};

class ProfileStore {
  constructor() {
    makeAutoObservable(this);
  }

  isPrivileged: boolean = false

  profile: IAccount = defaultIAccount;
  setProfile = (profile: IAccount) => {
    this.profile = profile;
    this.isPrivileged = profile.user.authorities.includes("ROLE_OWNER") || profile.user.authorities.includes("ROLE_MANAGER")
  };

  reset = () => {
    this.profile = defaultIAccount;
  };
}

export const profileStore = new ProfileStore();
