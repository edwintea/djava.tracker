import {
  Grid,
  lighten,
  List,
  ListItem,
  SvgIconTypeMap,
  Typography,
} from "@mui/material";
import { lightText } from "../../../constants/Colours";
import { AdminPanelSettings, Business, Lan } from "@mui/icons-material";
import React, { Dispatch, SetStateAction, useState } from "react";
import Content from "../../layouts/Content";
import { OverridableComponent } from "@mui/material/OverridableComponent";
import Tile from "../../layouts/Tile";
import Organisation from "./Organisation";
import Department from "./Department";
import UserManagement from "./UserManagement";

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

const Settings = () => {
  const [view, setView] = useState("Organisation");

  const viewRenderer = () => {
    switch (view) {
      case "Organisation":
        return <Organisation />;
      case "User Management":
        return <UserManagement />;
      case "Department & Positions":
        return <Department />;
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
        Settings
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
              Settings Menu
            </Typography>
            <List>
              <SettingsNavItem
                currentView={view}
                title={"Organisation"}
                Icon={Business}
                setView={setView}
              />
              <SettingsNavItem
                currentView={view}
                title={"User Management"}
                Icon={AdminPanelSettings}
                setView={setView}
              />
              <SettingsNavItem
                currentView={view}
                title={"Department & Positions"}
                Icon={Lan}
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

export default Settings;
