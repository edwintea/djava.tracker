import { Box, Paper, Typography } from "@mui/material";
import * as React from "react";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  registerEmployee,
  registerEmployeeOrg,
} from "../../../api/service/AuthenticationApi";
import { background } from "../../../constants/Colours";
import { appName } from "../../../config/AppConfig";
import {
  RegisterStepEnum,
  RegisterTypeEnum,
} from "../../../constants/RegisterEnum";
import RegisterType from "./RegisterType";
import RegisterUserDetails from "./RegisterUserDetails";
import RegisterOrgDetails from "./RegisterOrgDetails";
import { uiStore } from "../../../stores/UIStore";
import { useQuery } from "../../../utility/Hooks";

const Register = () => {
  const navigate = useNavigate();
  const query = useQuery();
  const organisationQS = query.get("organisation");

  const [view, setView] = useState<RegisterStepEnum>(
    RegisterStepEnum.REGISTER_TYPE
  );
  const [organisationName, setOrganisationName] = useState("");
  const [organisationCode, setOrganisationCode] = useState("");
  const [type, setType] = useState<RegisterTypeEnum>(RegisterTypeEnum.EMPLOYEE);
  const [login, setLogin] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  useEffect(() => {
    if (organisationQS && organisationQS.length > 0) {
      handleRegisterType(RegisterTypeEnum.EMPLOYEE);
    }
  }, [organisationQS]);

  const handleRegisterType = (type: RegisterTypeEnum) => {
    setType(type);
    setView(RegisterStepEnum.REGISTER_ORG_DETAILS);
  };

  const handleGoNext = (currentStep: RegisterStepEnum) => {
    switch (currentStep) {
      default:
        setView(RegisterStepEnum.REGISTER_USER_DETAILS);
    }
  };

  const handleGoBack = (currentStep: RegisterStepEnum) => {
    switch (currentStep) {
      case RegisterStepEnum.REGISTER_TYPE:
        navigate("/");
        break;
      case RegisterStepEnum.REGISTER_ORG_DETAILS:
        setView(RegisterStepEnum.REGISTER_TYPE);
        break;
      case RegisterStepEnum.REGISTER_USER_DETAILS:
        setView(RegisterStepEnum.REGISTER_ORG_DETAILS);
        break;
      default:
        navigate("/");
    }
  };

  const onHandleRegister = async () => {
    switch (type) {
      case RegisterTypeEnum.ORGANISATION:
        const employeeOrgResult = await registerEmployeeOrg({
          login,
          email,
          password,
          firstName,
          lastName,
          organisationName,
        });

        if (employeeOrgResult.status === 201) {
          uiStore.setSnackbarShow(true, "Registered successfully!", "success");
          navigate("/");
        }
        if (employeeOrgResult.status === 400 && employeeOrgResult.data) {
          if ((employeeOrgResult.data as any).errorKey === "userexists") {
            uiStore.setSnackbarShow(true, "Username already exists!", "error");
          }
        }
        break;
      case RegisterTypeEnum.EMPLOYEE:
        const employeeResult = await registerEmployee({
          login,
          email,
          password,
          firstName,
          lastName,
          organisationCode,
        });

        if (employeeResult.status === 201) {
          uiStore.setSnackbarShow(true, "Registered successfully!", "success");
          navigate("/");
        }
        break;
    }
  };

  const registerViewRenderer = (step: RegisterStepEnum) => {
    switch (step) {
      case RegisterStepEnum.REGISTER_TYPE:
        return (
          <RegisterType
            handleRegisterType={handleRegisterType}
            handleGoBack={handleGoBack}
          />
        );
      case RegisterStepEnum.REGISTER_ORG_DETAILS:
        return (
          <RegisterOrgDetails
            type={type}
            organisationCode={organisationCode}
            setOrganisationCode={setOrganisationCode}
            organisationName={organisationName}
            setOrganisationName={setOrganisationName}
            handleGoNext={handleGoNext}
            handleGoBack={handleGoBack}
            organisationQS={organisationQS}
          />
        );
      case RegisterStepEnum.REGISTER_USER_DETAILS:
        return (
          <RegisterUserDetails
            login={login}
            firstName={firstName}
            lastName={lastName}
            email={email}
            password={password}
            organisationName={organisationName}
            setLogin={setLogin}
            setFirstName={setFirstName}
            setLastName={setLastName}
            setEmail={setEmail}
            setPassword={setPassword}
            onHandleRegister={onHandleRegister}
            handleGoBack={handleGoBack}
            organisationQS={organisationQS}
          />
        );
      default:
        return <></>;
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
        {registerViewRenderer(view)}
      </Paper>
    </Box>
  );
};

export default Register;
