import { Box, Button, TextField, Typography } from "@mui/material";
import * as React from "react";
import { Dispatch, SetStateAction } from "react";
import { AccountCircle, AlternateEmail, Badge, Key } from "@mui/icons-material";
import { useNavigate } from "react-router-dom";
import { RegisterStepEnum } from "../../../constants/RegisterEnum";

const RegisterUserDetails = ({
  login,
  firstName,
  lastName,
  email,
  password,
  organisationName,
  setLogin,
  setFirstName,
  setLastName,
  setEmail,
  setPassword,
  onHandleRegister,
  handleGoBack,
  organisationQS,
}: {
  login: string;
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  organisationName: string;
  setLogin: Dispatch<SetStateAction<string>>;
  setFirstName: Dispatch<SetStateAction<string>>;
  setLastName: Dispatch<SetStateAction<string>>;
  setEmail: Dispatch<SetStateAction<string>>;
  setPassword: Dispatch<SetStateAction<string>>;
  onHandleRegister: () => void;
  handleGoBack: (currentStep: RegisterStepEnum) => void;
  organisationQS: string | null;
}) => {
  const navigate = useNavigate();
  const showBackButton = organisationQS === null;

  return (
    <>
      <Typography fontSize={"small"} letterSpacing={2} mb={1}>
        {organisationName}
      </Typography>
      <Typography
        textTransform={"uppercase"}
        fontSize={"small"}
        letterSpacing={2}
      >
        Employee Details
      </Typography>
      <Box sx={{ display: "flex", alignItems: "flex-end" }} mb={1}>
        <AccountCircle sx={{ color: "action.active", mr: 1, my: 0.5 }} />
        <TextField
          id="username"
          label="username"
          variant="standard"
          value={login}
          onChange={(e) => setLogin(e.currentTarget.value)}
        />
      </Box>
      <Box sx={{ display: "flex", alignItems: "flex-end" }} mb={1}>
        <Badge sx={{ color: "action.active", mr: 1, my: 0.5 }} />
        <TextField
          id="firstName"
          label="first name"
          variant="standard"
          value={firstName}
          onChange={(e) => setFirstName(e.currentTarget.value)}
        />
      </Box>
      <Box sx={{ display: "flex", alignItems: "flex-end" }} mb={1}>
        <Badge sx={{ color: "action.active", mr: 1, my: 0.5 }} />
        <TextField
          id="lastName"
          label="last name"
          variant="standard"
          value={lastName}
          onChange={(e) => setLastName(e.currentTarget.value)}
        />
      </Box>
      <Box sx={{ display: "flex", alignItems: "flex-end" }} mb={1}>
        <AlternateEmail sx={{ color: "action.active", mr: 1, my: 0.5 }} />
        <TextField
          id="email"
          label="email"
          variant="standard"
          type={"email"}
          value={email}
          onChange={(e) => setEmail(e.currentTarget.value)}
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
      <Button sx={{ mt: 4 }} variant="contained" onClick={onHandleRegister}>
        Register
      </Button>
      {showBackButton && (
        <Button
          sx={{ mt: 2 }}
          size={"small"}
          variant={"text"}
          onClick={() => handleGoBack(RegisterStepEnum.REGISTER_USER_DETAILS)}
        >
          Back
        </Button>
      )}
      <Button
        sx={{ mt: showBackButton ? 0 : 2 }}
        size={"small"}
        variant={"text"}
        onClick={() => navigate("/")}
      >
        Sign In
      </Button>
    </>
  );
};

export default RegisterUserDetails;
