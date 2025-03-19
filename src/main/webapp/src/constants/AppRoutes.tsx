import Login from "../components/pages/authentication/Login";
import Dashboard from "../components/pages/dashboard/Dashboard";
import Employees from "../components/pages/employees/Employees";
import Assignments from "../components/pages/clients/Clients";
import Newsletters from "../components/pages/newsletters/Newsletters";
import Settings from "../components/pages/settings/Settings";
import Profile from "../components/pages/profile/Profile";
import Register from "../components/pages/authentication/Register";
import EmployeeUpdate from "../components/pages/employees/EmployeeUpdate";
import LeaveRequest from "../components/pages/leaveRequests/LeaveRequest";

interface IAppRoute {
  path: string;
  element: JSX.Element;
  secure: boolean;
}

export const pages: IAppRoute[] = [
  {
    path: "/",
    element: <Login />,
    secure: false,
  },
  {
    path: "/register",
    element: <Register />,
    secure: false,
  },
  {
    path: "/dashboard",
    element: <Dashboard />,
    secure: true,
  },
  {
    path: "/employees",
    element: <Employees />,
    secure: true,
  },
  {
    path: "/employees/update/:employeeId",
    element: <EmployeeUpdate />,
    secure: true,
  },
  {
    path: "/assignments",
    element: <Assignments />,
    secure: true,
  },
  {
    path: "/newsletters",
    element: <Newsletters />,
    secure: true,
  },
  {
    path: "/leave-requests",
    element: <LeaveRequest />,
    secure: true,
  },
  {
    path: "/profile",
    element: <Profile />,
    secure: true,
  },
  {
    path: "/settings",
    element: <Settings />,
    secure: true,
  },
];
