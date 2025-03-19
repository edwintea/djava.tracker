import { makeAutoObservable } from "mobx";
import {
  defaultIEmployeeAssignment,
  IEmployeeAssignment,
} from "./EmployeeAssignmentStore";

export interface IEmployee {
  id: string;
  fullName: string;
  isAssigned: boolean;
  type: string;
  positionName: string;
  departmentName: string;
  position: string;
  department: string;
  employeeAssignment?: IEmployeeAssignment;
  user: string;
  reportsTo: string;
  organisation: string;
  sinceDate: string;
  isAssignable: boolean;
}

export const defaultIEmployee: IEmployee = {
  id: "",
  fullName: "",
  isAssigned: false,
  isAssignable: false,
  employeeAssignment: defaultIEmployeeAssignment,
  organisation: "",
  positionName: "",
  departmentName: "",
  position: "",
  department: "",
  reportsTo: "",
  sinceDate: "",
  type: "",
  user: "",
};

export const EmployeeTypes = [
  {
    value: "PERMANENT",
    name: "Permanent",
  },
  {
    value: "INTERN",
    name: "Intern",
  },
  {
    value: "CONTRACT",
    name: "Contract",
  },
  {
    value: "FREELANCE",
    name: "Freelance",
  },
  {
    value: "PART_TIME",
    name: "Part Time",
  },
];

class EmployeeStore {
  constructor() {
    makeAutoObservable(this);
  }

  employees: IEmployee[] = [];
  setEmployees = (employees: IEmployee[]) => {
    this.employees = employees;
  };
}

export const employeeStore = new EmployeeStore();
