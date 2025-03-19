import api from "../Api";
import { IProfile } from "../../stores/ProfileStore";
import { IEmployee } from "../../stores/EmployeeStore";

interface ICredentials {
  username: string;
  password: string;
  rememberMe: false;
}

interface IRegister {
  login: string;
  firstName: string;
  lastName: string;
  email: string;
  password: string;
}

interface IAuthenticate {
  idToken: string;
  user: IProfile;
  employee: IEmployee;
}

interface IRegisterOrg extends IRegister {
  organisationName: string;
}

interface IRegisterEmployee extends IRegister {
  organisationCode: string;
}

export const login = (data: ICredentials) => {
  return api.post<IAuthenticate>("/authenticate", data);
};

export const register = (data: IRegister) => {
  return api.post("/register", data);
};

export const registerEmployeeOrg = (data: IRegisterOrg) => {
  return api.post("/register/employee/org", data);
};

export const registerEmployee = (data: IRegisterEmployee) => {
  return api.post("/register/employee", data);
};
