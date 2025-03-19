import { Box, Paper, SvgIconTypeMap, Typography } from "@mui/material";
import * as React from "react";
import { OverridableComponent } from "@mui/material/OverridableComponent";
import { useLocation, useNavigate } from "react-router-dom";
import { lighterText, primary } from "../../constants/Colours";
import { authenticationStore } from "../../stores/AuthenticationStore";
import { observer } from "mobx-react";
import { privilegeMobileNavItems, basicMobileNavItems } from "../../constants/NavItems";
import {profileStore} from "../../stores/ProfileStore";

const BottomTabNavItem = ({
  name,
  Icon,
}: {
  name: string;
  Icon: OverridableComponent<SvgIconTypeMap> & { muiName: string };
}) => {
  const location = useLocation();
  const navigate = useNavigate();

  const handleOnNavigate = () => {
    navigate(`/${name.toLowerCase()}`);
  };

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
        borderRadius: 10,
        padding: 0.5,
        color:
          location.pathname === `/${name.toLowerCase().replace(" ", "-")}`
            ? "white"
            : lighterText,
      }}
      onClick={handleOnNavigate}
    >
      <Icon fontSize={"small"} />
      <Typography variant={"caption"}>{name}</Typography>
    </Box>
  );
};

const BottomNavigator = () => {
  const { isAuthenticated } = authenticationStore;
  const { isPrivileged } = profileStore

  const navItems = isPrivileged ? privilegeMobileNavItems : basicMobileNavItems

  return isAuthenticated ? (
    <Paper
      sx={{
        display: { xs: "flex", sm: "none" },
        alignItems: "center",
        justifyContent: "space-evenly",
        position: "absolute",
        left: 20,
        right: 20,
        bottom: 20,
        height: 65,
        backgroundColor: primary,
        borderRadius: 20,
      }}
      elevation={20}
    >
      {navItems.map((navItem) => (
        <BottomTabNavItem
          key={`${navItem.title}-bottom-nav`}
          name={navItem.title}
          Icon={navItem.Icon}
        />
      ))}
    </Paper>
  ) : (
    <></>
  );
};

export default observer(BottomNavigator);
