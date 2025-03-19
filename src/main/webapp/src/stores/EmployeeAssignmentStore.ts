import { makeAutoObservable } from "mobx";

export interface IEmployeeAssignment {
  id?: string;
  active: boolean;
  main: boolean;
  client: string;
  startDate: string;
  toDate: string;
  assignmentType: string;
  description: string;
  clientName: string;
  assignment: string;
  user: string;
  organisation: string;
}

export const defaultIEmployeeAssignment: IEmployeeAssignment = {
  id: "",
  active: false,
  main: false,
  client: "",
  startDate: "",
  toDate: "",
  assignmentType: "",
  description: "",
  clientName: "",
  assignment: "",
  user: "",
  organisation: "",
};

class EmployeeAssignmentStore {
  constructor() {
    makeAutoObservable(this);
  }

  assignments: IEmployeeAssignment[] = [];
  setAssignments = (assignments: IEmployeeAssignment[]) => {
    this.assignments = assignments;
  };

  employeeAssignments: IEmployeeAssignment[] = [];
  setEmployeeAssignments = (employeeAssignments: IEmployeeAssignment[]) => {
    this.employeeAssignments = employeeAssignments;
  };
}

export const employeeAssignmentStore = new EmployeeAssignmentStore();
