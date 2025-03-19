import {CalendarMonth, Dashboard, Email, Groups, Settings, Work} from "@mui/icons-material";
import { OverridableComponent } from "@mui/material/OverridableComponent";
import { SvgIconTypeMap } from "@mui/material";

interface INavItems {
  title: string;
  Icon: OverridableComponent<SvgIconTypeMap> & { muiName: string };
}

export const privilegedDesktopNavItems: INavItems[] = [
  {
    title: "Dashboard",
    Icon: Dashboard,
  },
  {
    title: "Employees",
    Icon: Groups,
  },
  {
    title: "Assignments",
    Icon: Work,
  },
  {
    title: "Newsletters",
    Icon: Email,
  },
  {
    title: "Settings",
    Icon: Settings,
  },
];

export const basicDesktopNavItems: INavItems[] = [
  {
    title: "Dashboard",
    Icon: Dashboard,
  },
  {
    title: "Assignments",
    Icon: Work,
  },
  {
    title: "Leave Requests",
    Icon: CalendarMonth,
  },
  {
    title: "Settings",
    Icon: Settings,
  },
];

export const privilegeMobileNavItems: INavItems[] = [
  {
    title: "Dashboard",
    Icon: Dashboard,
  },
  {
    title: "Employees",
    Icon: Groups,
  },
  {
    title: "Assignments",
    Icon: Work,
  },
  {
    title: "Newsletters",
    Icon: Email,
  },
];

export const basicMobileNavItems: INavItems[] = [
  {
    title: "Dashboard",
    Icon: Dashboard,
  },
  {
    title: "Assignments",
    Icon: Work,
  },
  {
    title: "Leave Requests",
    Icon: CalendarMonth,
  },
];
