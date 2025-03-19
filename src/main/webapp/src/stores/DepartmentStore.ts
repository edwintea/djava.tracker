import { makeAutoObservable } from "mobx";

export interface IDepartment {
  id?: string;
  name: string;
  organisation: string;
}

class DepartmentStore {
  constructor() {
    makeAutoObservable(this);
  }

  departments: IDepartment[] = [];
  setDepartments = (departments: IDepartment[]) => {
    this.departments = departments;
  };
}

export const departmentStore = new DepartmentStore();
