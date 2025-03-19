import { makeAutoObservable } from "mobx";

export interface IPosition {
  id?: string;
  name: string;
  department: string;
  departmentName?: string;
  organisation: string;
}

export interface IPositionEmployeeCount extends IPosition {
  employeeCount: number;
}

class PositionStore {
  constructor() {
    makeAutoObservable(this);
  }

  positions: IPositionEmployeeCount[] = [];
  setPositions = (positions: IPositionEmployeeCount[]) => {
    this.positions = positions;
  };
}

export const positionStore = new PositionStore();
