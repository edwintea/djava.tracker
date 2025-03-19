import { departmentStore, IDepartment } from "../../stores/DepartmentStore";
import api from "../Api";
import { uiStore } from "../../stores/UIStore";

export const createDepartment = async (data: IDepartment) => {
  const result = await api.post<IDepartment>("/departments", data);

  if (result.status === 201 && result.data) {
    uiStore.setSnackbarShow(
      true,
      "Created department successfully!",
      "success"
    );
  }
};

export const getDepartments = async () => {
  const result = await api.get<IDepartment[]>("/departments/specific");

  if (result.status === 200 && result.data) {
    departmentStore.setDepartments(result.data);
  }
};
