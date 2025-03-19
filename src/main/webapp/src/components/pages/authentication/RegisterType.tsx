import { Button, ButtonBase, Paper, Typography } from "@mui/material";
import { Business, PersonAdd } from "@mui/icons-material";
import * as React from "react";
import {
  RegisterStepEnum,
  RegisterTypeEnum,
} from "../../../constants/RegisterEnum";

const RegisterType = ({
  handleRegisterType,
  handleGoBack,
}: {
  handleRegisterType: (type: RegisterTypeEnum) => void;
  handleGoBack: (currentStep: RegisterStepEnum) => void;
}) => {
  return (
    <>
      <Typography
        textTransform={"uppercase"}
        fontSize={"small"}
        letterSpacing={2}
      >
        Register As An
      </Typography>
      <ButtonBase
        sx={{ borderRadius: 8, mt: 2, mb: 2 }}
        onClick={() => handleRegisterType(RegisterTypeEnum.ORGANISATION)}
      >
        <Paper elevation={2} sx={{ width: 200, borderRadius: 8, p: 4 }}>
          <Business />
          <Typography variant={"subtitle1"} mt={1}>
            Organisation
          </Typography>
        </Paper>
      </ButtonBase>
      <ButtonBase
        sx={{ borderRadius: 8 }}
        onClick={() => handleRegisterType(RegisterTypeEnum.EMPLOYEE)}
      >
        <Paper elevation={2} sx={{ width: 200, borderRadius: 8, p: 4 }}>
          <PersonAdd />
          <Typography variant={"subtitle1"} mt={1}>
            Employee
          </Typography>
        </Paper>
      </ButtonBase>
      <Button
        sx={{ mt: 2 }}
        size={"small"}
        variant={"text"}
        onClick={() => handleGoBack(RegisterStepEnum.REGISTER_TYPE)}
      >
        Back
      </Button>
    </>
  );
};

export default RegisterType;
