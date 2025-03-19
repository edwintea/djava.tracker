import api from "../Api";
import {
  employeeAssignmentStore,
  IEmployeeAssignment,
} from "../../stores/EmployeeAssignmentStore";
import { uiStore } from "../../stores/UIStore";

export const createEmployeeAssignment = async (data: IEmployeeAssignment) => {
  const result = await api.post("/employee-assignments", data);

  if (result.status === 201 && result.data) {
    uiStore.setSnackbarShow(
      true,
      "Created assignment successfully!",
      "success"
    );
  }

  return result;
};

export const getEmployeeAssignments = async (userId: string) => {
  const result = await api.get<IEmployeeAssignment[]>(
    `/employee-assignments/specific/${userId}`
  );

  if (result.status === 200 && result.data) {
    employeeAssignmentStore.setEmployeeAssignments(result.data);

    return result;
  }
  return null;
};
