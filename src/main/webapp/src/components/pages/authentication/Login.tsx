import { Box, Button, Paper, TextField, Typography } from "@mui/material";
import { AccountCircle, Key } from "@mui/icons-material";
import * as React from "react";
import { authenticationStore } from "../../../stores/AuthenticationStore";
import { useNavigate } from "react-router-dom";
import { login } from "../../../api/service/AuthenticationApi";
import { useState } from "react";
import { setStorage } from "../../../utility/Storage";
import { uiStore } from "../../../stores/UIStore";
import api from "../../../api/Api";
import { appName, organisationHeader } from "../../../config/AppConfig";
import { background } from "../../../constants/Colours";
import { profileStore } from "../../../stores/ProfileStore";
import { getOrganisationBase } from "../../../api/service/OrganisationApi";

const Login = () => {
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const { setIsAuthenticated } = authenticationStore;

  const handleOnLogin = async () => {
    const result = await login({
      username,
      password,
      rememberMe: false,
    });

    if (result.status === 200 && result.data) {
      api.setHeader("Authorization", `Bearer ${result.data.idToken}`);
      api.setHeader(organisationHeader, result.data.employee.organisation);
      profileStore.setProfile({
        user: result.data.user,
        employee: result.data.employee,
      });
      setStorage("token", result.data.idToken);
      getOrganisationBase().then(() => {
        setIsAuthenticated(true);
        navigate("/dashboard");
      });
    }

    if (result.status === 401) {
      uiStore.setSnackbarShow(
        true,
        "Please check your credentials again!",
        "error"
      );
    }
  };

  return (
    <Box
      sx={{
        display: "flex",
        height: "100vh",
        width: "100vw",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        backgroundColor: background,
      }}
    >
      <Paper
        sx={{
          display: "flex",
          flexDirection: "column",
          padding: 4,
          textAlign: "center",
          borderRadius: 8,
        }}
        elevation={2}
      >
        <Typography
          sx={{ mb: 4 }}
          variant={"h6"}
          textTransform={"uppercase"}
          letterSpacing={1.5}
        >
          {appName}
        </Typography>
        <Box sx={{ display: "flex", alignItems: "flex-end" }} mb={1}>
          <AccountCircle sx={{ color: "action.active", mr: 1, my: 0.5 }} />
          <TextField
            id="username"
            label="username"
            variant="standard"
            value={username}
            onChange={(e) => setUsername(e.currentTarget.value)}
          />
        </Box>
        <Box sx={{ display: "flex", alignItems: "flex-end" }} mb={1}>
          <Key sx={{ color: "action.active", mr: 1, my: 0.5 }} />
          <TextField
            id="password"
            label="password"
            variant="standard"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.currentTarget.value)}
          />
        </Box>
        <Button sx={{ mt: 4 }} variant="contained" onClick={handleOnLogin}>
          Sign In
        </Button>
        <Button
          sx={{ mt: 2 }}
          size={"small"}
          variant={"text"}
          onClick={() => navigate("/register")}
        >
          Register
        </Button>
      </Paper>
    </Box>
  );
};

export default Login;
