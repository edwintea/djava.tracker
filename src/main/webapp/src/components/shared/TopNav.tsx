import { ReactNode } from "react";
import CssBaseline from "@mui/material/CssBaseline";
import AppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import IconButton from "@mui/material/IconButton";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import { AccountCircle, Logout, Settings } from "@mui/icons-material";
import { Menu, MenuItem } from "@mui/material";
import * as React from "react";
import { authenticationStore } from "../../stores/AuthenticationStore";
import { observer } from "mobx-react";
import { useNavigate } from "react-router-dom";
import { clearStorage } from "../../utility/Storage";
import api from "../../api/Api";
import { pastelRed } from "../../constants/Colours";
import {basicDesktopNavItems, privilegedDesktopNavItems} from "../../constants/NavItems";
import { appName, organisationHeader } from "../../config/AppConfig";
import { organisationStore } from "../../stores/OrganisationStore";
import {profileStore} from "../../stores/ProfileStore";

const settings = ["Profile", "Logout"];

const TopBar = ({ children }: { children: ReactNode }) => {
  const { organisation } = organisationStore;
  const navigate = useNavigate();
  const [anchorElUser, setAnchorElUser] = React.useState<null | HTMLElement>(
    null
  );
  const {isPrivileged} = profileStore

  const navItems = isPrivileged ? privilegedDesktopNavItems : basicDesktopNavItems

  const handleOpenUserMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorElUser(event.currentTarget);
  };

  const handleCloseUserMenu = (setting: string) => {
    switch (setting) {
      case "Profile":
        navigate("/profile");
        break;
      case "Settings":
        navigate("/settings");
        break;
      case "Logout":
        //Clear the store
        //Clear the storage
        //Clear any auths
        clearStorage("token");
        api.deleteHeader(organisationHeader);
        api.deleteHeader("Authorization");
        authenticationStore.setIsAuthenticated(false);
        break;
    }
    setAnchorElUser(null);
  };

  const handleNavigate = (item: string) => {
    navigate(item.toLowerCase().replace(" ", "-"));
  };

  return (
    <Box sx={{ display: "flex" }}>
      <CssBaseline />
      {authenticationStore.isAuthenticated ? (
        <>
          <AppBar component="nav">
            <Toolbar>
              <Box
                sx={{
                  flexGrow: 1,
                  flexDirection: "column",
                  display: { xs: "flex", sm: "none" },
                  justifyContent: "center",
                  alignItems: "center",
                }}
              >
                <Typography
                  variant={"h6"}
                  component="div"
                  sx={{
                    textTransform: "uppercase",
                    letterSpacing: 3,
                    textAlign: "center",
                  }}
                >
                  {appName}
                </Typography>
                <Typography fontSize={"x-small"} letterSpacing={2} mt={-0.5}>
                  {organisation.name}
                </Typography>
              </Box>
              <IconButton
                sx={{
                  display: { xs: "block", sm: "none" },
                  position: "absolute",
                  left: 0,
                  ml: 2,
                  color: "white",
                }}
                onClick={() => navigate("/profile")}
              >
                <AccountCircle fontSize={"small"} />
              </IconButton>
              <IconButton
                sx={{
                  display: { xs: "block", sm: "none" },
                  position: "absolute",
                  left: 0,
                  ml: 6,
                  color: "white",
                }}
                onClick={() => navigate("/settings")}
              >
                <Settings fontSize={"small"} />
              </IconButton>
              <IconButton
                sx={{
                  display: { xs: "block", sm: "none" },
                  position: "absolute",
                  right: 0,
                  mr: 2,
                  color: pastelRed,
                }}
                onClick={() => handleCloseUserMenu("Logout")}
              >
                <Logout fontSize={"small"} />
              </IconButton>
              <Box
                sx={{
                  flexGrow: 1,
                  display: { xs: "none", sm: "flex" },
                  flexDirection: "column",
                }}
              >
                <Typography
                  variant={"h6"}
                  component="div"
                  sx={{
                    textTransform: "uppercase",
                    letterSpacing: 3,
                  }}
                >
                  {appName}
                </Typography>
                <Typography fontSize={"x-small"} letterSpacing={2} mt={-0.5}>
                  {organisation.name}
                </Typography>
              </Box>
              <Box sx={{ display: { xs: "none", sm: "block" } }}>
                {navItems.map((item) => {
                  const { title, Icon } = item;
                  return (
                    <Button
                      key={title}
                      sx={{ color: "#fff", textTransform: "none", mr: 1.5 }}
                      startIcon={<Icon />}
                      onClick={() => handleNavigate(title)}
                    >
                      {title}
                    </Button>
                  );
                })}
              </Box>
              <Box sx={{ display: { xs: "none", md: "flex" } }}>
                <IconButton
                  size="large"
                  edge="end"
                  aria-label="account of current user"
                  aria-haspopup="true"
                  color="inherit"
                  onClick={handleOpenUserMenu}
                >
                  {/*<Badge badgeContent={4} color="error">*/}
                  <AccountCircle />
                  {/*</Badge>*/}
                </IconButton>
                <Menu
                  sx={{ mt: "45px" }}
                  id="menu-appbar"
                  anchorEl={anchorElUser}
                  anchorOrigin={{
                    vertical: "top",
                    horizontal: "right",
                  }}
                  keepMounted
                  transformOrigin={{
                    vertical: "top",
                    horizontal: "right",
                  }}
                  open={Boolean(anchorElUser)}
                  onClose={handleCloseUserMenu}
                >
                  {settings.map((setting) => (
                    <MenuItem
                      key={setting}
                      onClick={() => handleCloseUserMenu(setting)}
                    >
                      <Typography textAlign="center">{setting}</Typography>
                    </MenuItem>
                  ))}
                </Menu>
              </Box>
            </Toolbar>
          </AppBar>
          <Box
            component="main"
            sx={{
              p: 3,
              height: "100vh",
              width: "100vw",
              overflowY: "scroll",
            }}
          >
            <Toolbar />
            {children}
            <Toolbar sx={{ display: { xs: "none", sm: "block" } }} />
            <Box sx={{ display: { xs: "block", sm: "none", height: 85 } }} />
          </Box>
        </>
      ) : (
        <>{children}</>
      )}
    </Box>
  );
};

export default observer(TopBar);
