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
import React, { Dispatch, SetStateAction, useEffect, useState } from "react";
import { observer } from "mobx-react";
import { AddCircle, ManageSearch, SavedSearch } from "@mui/icons-material";
import ClientSearch from "./ClientSearch";
import Tile from "../../layouts/Tile";
import { OverridableComponent } from "@mui/material/OverridableComponent";
import AddClient from "./AddClient";
import AssignmentSearch from "./AssignmentSearch";
import AddAssignment from "./AddAssignments";

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

const PrivilegedAssignment = () => {
  useEffect(() => {}, []);

  const [view, setView] = useState("Client Search");

  const viewRenderer = () => {
    switch (view) {
      case "Add Client":
        return <AddClient />;
      case "Client Search":
        return <ClientSearch />;
      case "Add Assignment":
        return <AddAssignment />;
      case "Assignment Search":
        return <AssignmentSearch />;
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
        Assignments
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
              Client Menu
            </Typography>
            <List>
              <SettingsNavItem
                currentView={view}
                title={"Add Client"}
                Icon={AddCircle}
                setView={setView}
              />
              <SettingsNavItem
                currentView={view}
                title={"Client Search"}
                Icon={SavedSearch}
                setView={setView}
              />
            </List>
            <Typography
              variant={"caption"}
              color={lightText}
              textTransform={"uppercase"}
              mb={1}
            >
              Assignment Menu
            </Typography>
            <List>
              <SettingsNavItem
                currentView={view}
                title={"Add Assignment"}
                Icon={AddCircle}
                setView={setView}
              />
              <SettingsNavItem
                currentView={view}
                title={"Assignment Search"}
                Icon={ManageSearch}
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

export default observer(PrivilegedAssignment);
