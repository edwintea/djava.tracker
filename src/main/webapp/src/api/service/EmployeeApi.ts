import api from "../Api";
import { employeeStore, IEmployee } from "../../stores/EmployeeStore";
import { uiStore } from "../../stores/UIStore";

export const updateEmployee = async (data: IEmployee) => {
  const result = await api.put("/employees", data);

  if (result.status === 200) {
    uiStore.setSnackbarShow(
      true,
      `Updated ${data.fullName} successfully!`,
      "success"
    );
  }
};

export const getEmployeesPaged = async (page: number, size: number) => {
  const result = await api.get<IEmployee[]>(
    `/employees/specific?page=${page}&size=${size}`
  );

  if (result.status === 200 && result.data) {
    employeeStore.setEmployees(result.data);

    return result;
  }
  return null;
};
