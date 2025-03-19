import { makeAutoObservable } from "mobx";

export interface IAssignment {
  id?: string;
  clientName?: string;
  client: string;
  assignmentType: string;
  description: string;
  organisation?: string;
}

export interface IAssignmentEmployeeCount extends IAssignment {
  employeeCount: number;
}

class AssignmentStore {
  constructor() {
    makeAutoObservable(this);
  }

  assignments: IAssignmentEmployeeCount[] = [];
  setAssignments = (assignments: IAssignmentEmployeeCount[]) => {
    this.assignments = assignments;
  };

  assignmentMinimal: IAssignment[] = [];
  setAssignmentMinimal = (assignmentMinimal: IAssignment[]) => {
    this.assignmentMinimal = assignmentMinimal;
  };
}

export const assignmentStore = new AssignmentStore();
