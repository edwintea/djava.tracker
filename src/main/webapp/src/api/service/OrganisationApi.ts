import api from "../Api";
import {
  IDashboardCombo,
  IOrganisationBase,
  IOrganisationMinimal,
  organisationStore,
} from "../../stores/OrganisationStore";
import { employeeStore } from "../../stores/EmployeeStore";
import { departmentStore } from "../../stores/DepartmentStore";
import { positionStore } from "../../stores/PositionStore";
import { clientStore } from "../../stores/ClientStore";
import { assignmentStore } from "../../stores/AssignmentStore";
import { newsletterStore } from "../../stores/NewsletterStore";

export const getOrganisationByCode = async (code: string) => {
  return await api.get<IOrganisationMinimal>(`/organisations/joinCode/${code}`);
};

export const getOrganisationBase = async () => {
  const result = await api.get<IOrganisationBase>(`/organisations/base`);

  if (result.status === 200 && result.data) {
    const data = result.data;
    organisationStore.setOrganisation(data.organisation);
    employeeStore.setEmployees(data.employees);
    departmentStore.setDepartments(data.departments);
    positionStore.setPositions(data.positions);
    clientStore.setClients(data.clients);
    assignmentStore.setAssignments(data.assignments);
    newsletterStore.setNewsletters(data.newsletters);
  }
};

export const getDashboard = async () => {
  const result = await api.get<IDashboardCombo>(
    `/organisations/admin/dashboard`
  );

  if (result.status === 200 && result.data) {
    organisationStore.setDashboard(result.data);
  }
};
