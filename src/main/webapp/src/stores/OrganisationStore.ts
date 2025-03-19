import { makeAutoObservable } from "mobx";
import { IEmployee } from "./EmployeeStore";
import { IDepartment } from "./DepartmentStore";
import { IPositionEmployeeCount } from "./PositionStore";
import { IClientEmployeeCount } from "./ClientStore";
import { IAssignmentEmployeeCount } from "./AssignmentStore";
import { INewsletter } from "./NewsletterStore";

export interface IOrganisation {
  address: string;
  companyReg: string;
  id: string;
  joinCode: string;
  name: string;
}

export interface IOrganisationMinimal {
  name: string;
  joinCode: string;
}

export interface IOrganisationBase {
  organisation: IOrganisation;
  employees: IEmployee[];
  departments: IDepartment[];
  positions: IPositionEmployeeCount[];
  clients: IClientEmployeeCount[];
  assignments: IAssignmentEmployeeCount[];
  newsletters: INewsletter[];
}

export interface IDashboardCombo {
  clients: IClientEmployeeCount[];
  clientOverview: IClientOverview;
  employeeOverview: IEmployeeOverview;
  projects: IProjects[];
}

export interface IClient {
  id?: string;
  name: string;
  sinceDate: string;
  address: string;
  organisation: string;
}

export interface IProjects {
  clientName: string;
  status: string;
  employees: number;
  targetCompletion: string;
}

export interface IClientOverview {
  outsourcedBased: number;
  projectBased: number;
}

export interface IEmployeeOverview {
  totalEmployees: number;
  totalOnLeave: number;
}

const defaultIOrganisation: IOrganisation = {
  address: "",
  companyReg: "",
  id: "",
  joinCode: "",
  name: "",
};

const defaultIClientOverview: IClientOverview = {
  outsourcedBased: 0,
  projectBased: 0,
};

const defaultIEmployeeOverview: IEmployeeOverview = {
  totalEmployees: 0,
  totalOnLeave: 0,
};

export const defaultIDashboardCombo: IDashboardCombo = {
  clients: [],
  clientOverview: defaultIClientOverview,
  employeeOverview: defaultIEmployeeOverview,
  projects: [],
};

class OrganisationStore {
  constructor() {
    makeAutoObservable(this);
  }

  organisation: IOrganisation = defaultIOrganisation;
  setOrganisation = (organisation: IOrganisation) => {
    this.organisation = organisation;
  };

  dashboard: IDashboardCombo = defaultIDashboardCombo;
  setDashboard = (dashboard: IDashboardCombo) => {
    this.dashboard = dashboard;
  };
}

export const organisationStore = new OrganisationStore();
