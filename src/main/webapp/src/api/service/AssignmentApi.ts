import {
  assignmentStore,
  IAssignment,
  IAssignmentEmployeeCount,
} from "../../stores/AssignmentStore";
import api from "../Api";
import { uiStore } from "../../stores/UIStore";

export const createAssignment = async (data: IAssignment) => {
  const result = await api.post("/assignments", data);

  if (result.status === 201 && result.data) {
    uiStore.setSnackbarShow(
      true,
      "Created assignment successfully!",
      "success"
    );
  }
};

export const getAssignmentPageable = async (page: number, size: number) => {
  const result = await api.get<IAssignmentEmployeeCount[]>(
    `/assignments/specific?page=${page}&size=${size}`
  );

  if (result.status === 200 && result.data) {
    assignmentStore.setAssignments(result.data);

    return result;
  }
  return null;
};

export const getAssignmentMinimal = async () => {
  const result = await api.get<IAssignment[]>("/assignments/minimal/specific");

  if (result.status === 200 && result.data) {
    assignmentStore.setAssignmentMinimal(result.data);
    return result;
  }
  return null;
};
