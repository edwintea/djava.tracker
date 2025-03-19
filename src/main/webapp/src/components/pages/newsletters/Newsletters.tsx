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
import { AddCircle, Mail } from "@mui/icons-material";
import Tile from "../../layouts/Tile";
import { OverridableComponent } from "@mui/material/OverridableComponent";
import NewslettersTable from "./NewslettersTable";
import AddNewsletter from "./AddNewsletter";

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

const Newsletters = () => {
  const [view, setView] = useState("Newsletters");

  const viewRenderer = () => {
    switch (view) {
      case "Add Newsletter":
        return <AddNewsletter />;
      case "Newsletters":
        return <NewslettersTable />;
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
        Newsletters
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
              Newsletter Menu
            </Typography>
            <List>
              <SettingsNavItem
                currentView={view}
                title={"Add Newsletter"}
                Icon={AddCircle}
                setView={setView}
              />
              <SettingsNavItem
                currentView={view}
                title={"Newsletters"}
                Icon={Mail}
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

export default observer(Newsletters);
