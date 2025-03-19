import Content from "../../layouts/Content";
import {
  Grid,
  lighten,
  List,
  ListItem,
  SvgIconTypeMap,
  Typography,
} from "@mui/material";
import { lightText } from "../../../constants/Colours";
import React, { Dispatch, SetStateAction, useState } from "react";
import { observer } from "mobx-react";
import Tile from "../../layouts/Tile";
import {
  AccountTree,
  AddCircle,
  CalendarMonth,
  PersonSearch,
} from "@mui/icons-material";
import { OverridableComponent } from "@mui/material/OverridableComponent";
import EmployeeSearch from "./EmployeeSearch";
import AddDepartment from "./AddDepartment";
import AddPosition from "./AddPosition";
import PositionsTable from "./PositionsTable";
import LeaveRequestsTable from "./LeaveRequestsTable";

const SettingsNavItem = ({
  currentView,
  title,
  Icon,
  setView,
}: {
  currentView: string;
  title: string;
  Icon: OverridableComponent<SvgIconTypeMap<{}, "svg">> & { muiName: string };
  setView?: Dispatch<SetStateAction<string>>;
}) => {
  return (
    <ListItem
      button
      sx={{
        display: "flex",
        flexDirection: "row",
        alignItems: "center",
        borderRadius: 8,
        marginBottom: 1,
      }}
      onClick={setView ? () => setView(title) : undefined}
      style={{
        backgroundColor:
          currentView === title ? lighten(lightText, 0.9) : "transparent",
      }}
    >
      <Icon sx={{ mr: 4 }} />
      <Typography>{title}</Typography>
    </ListItem>
  );
};

const Employees = () => {
  const [view, setView] = useState("Employee Search");

  const viewRenderer = () => {
    switch (view) {
      case "Employee Search":
        return <EmployeeSearch />;
      case "Leave Requests":
        return <LeaveRequestsTable />;
      case "Add Department":
        return <AddDepartment />;
      case "Add Position":
        return <AddPosition />;
      case "Positions":
        return <PositionsTable />;
      default:
        return <></>;
    }
  };

  return (
    <Content>
      <Typography
        variant={"h6"}
        color={lightText}
        fontSize={"medium"}
        fontWeight={"bolder"}
        letterSpacing={2}
        textTransform={"uppercase"}
        flex={1}
      >
        Employees
      </Typography>
      <Grid container spacing={5} mt={0.5}>
        <Grid item xs={12} md={3}>
          <Tile>
            <Typography
              variant={"caption"}
              color={lightText}
              textTransform={"uppercase"}
              mb={1}
            >
              Employee Menu
            </Typography>
            <List>
              <SettingsNavItem
                currentView={view}
                title={"Employee Search"}
                Icon={PersonSearch}
                setView={setView}
              />
            </List>
            <Typography
              variant={"caption"}
              color={lightText}
              textTransform={"uppercase"}
              mb={1}
            >
              Annual Leave Menu
            </Typography>
            <List>
              <SettingsNavItem
                currentView={view}
                title={"Leave Requests"}
                Icon={CalendarMonth}
                setView={setView}
              />
            </List>
            <Typography
              variant={"caption"}
              color={lightText}
              textTransform={"uppercase"}
              mb={1}
            >
              Department Menu
            </Typography>
            <List>
              <SettingsNavItem
                currentView={view}
                title={"Add Department"}
                Icon={AddCircle}
                setView={setView}
              />
              <SettingsNavItem
                currentView={view}
                title={"Add Position"}
                Icon={AddCircle}
                setView={setView}
              />
              <SettingsNavItem
                currentView={view}
                title={"Positions"}
                Icon={AccountTree}
                setView={setView}
              />
            </List>
          </Tile>
        </Grid>
        <Grid item xs={12} md={9}>
          {viewRenderer()}
        </Grid>
      </Grid>
    </Content>
  );
};

export default observer(Employees);
