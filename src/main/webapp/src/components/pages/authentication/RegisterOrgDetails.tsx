import { Box, Button, TextField, Typography } from "@mui/material";
import { Business } from "@mui/icons-material";
import * as React from "react";
import { Dispatch, SetStateAction, useEffect } from "react";
import {
  RegisterStepEnum,
  RegisterTypeEnum,
} from "../../../constants/RegisterEnum";
import { getOrganisationByCode } from "../../../api/service/OrganisationApi";
import { uiStore } from "../../../stores/UIStore";

const OrganisationDetailsForm = ({
  organisationName,
  setOrganisationName,
}: {
  organisationName: string;
  setOrganisationName: Dispatch<SetStateAction<string>>;
}) => {
  return (
    <TextField
      id="organisationName"
      label="organisation name"
      variant="standard"
      value={organisationName}
      onChange={(e) => setOrganisationName(e.currentTarget.value)}
    />
  );
};

const OrganisationJoinForm = ({
  organisationCode,
  setOrganisationCode,
}: {
  organisationCode: string;
  setOrganisationCode: Dispatch<SetStateAction<string>>;
}) => {
  return (
    <TextField
      id="organisationCode"
      label="organisation code"
      variant="standard"
      value={organisationCode}
      onChange={(e) => setOrganisationCode(e.currentTarget.value)}
    />
  );
};

const RegisterOrgDetails = ({
  type,
  organisationName,
  setOrganisationName,
  organisationCode,
  setOrganisationCode,
  handleGoNext,
  handleGoBack,
  organisationQS,
}: {
  type: RegisterTypeEnum;
  organisationName: string;
  setOrganisationName: Dispatch<SetStateAction<string>>;
  organisationCode: string;
  setOrganisationCode: Dispatch<SetStateAction<string>>;
  handleGoNext: (currentStep: RegisterStepEnum) => void;
  handleGoBack: (currentStep: RegisterStepEnum) => void;
  organisationQS: string | null;
}) => {
  useEffect(() => {
    if (organisationQS) {
      setOrganisationCode(organisationQS);
      onHandleGoNext();
    }
  }, [organisationQS]);

  const onHandleGoNext = () => {
    switch (type) {
      case RegisterTypeEnum.ORGANISATION:
        handleGoNext(RegisterStepEnum.REGISTER_ORG_DETAILS);
        break;
      case RegisterTypeEnum.EMPLOYEE:
        getOrganisationByCode(organisationQS ?? organisationCode).then(
          (result) => {
            if (result.status === 200 && result.data) {
              setOrganisationName(result.data.name);
              handleGoNext(RegisterStepEnum.REGISTER_ORG_DETAILS);
            }
            if (result.status === 404) {
              uiStore.setSnackbarShow(
                true,
                "Organisation not found, please check your code again.",
                "warning"
              );
            }
          }
        );
        break;
    }
  };

  const organisationDetailsViewRenderer = () => {
    switch (type) {
      case RegisterTypeEnum.ORGANISATION:
        return (
          <OrganisationDetailsForm
            organisationName={organisationName}
            setOrganisationName={setOrganisationName}
          />
        );
      case RegisterTypeEnum.EMPLOYEE:
        return (
          <OrganisationJoinForm
            organisationCode={organisationCode}
            setOrganisationCode={setOrganisationCode}
          />
        );
      default:
        return <></>;
    }
  };

  return (
    <>
      <Typography
        textTransform={"uppercase"}
        fontSize={"small"}
        letterSpacing={2}
      >
        {type === RegisterTypeEnum.ORGANISATION
          ? "Organisation Details"
          : "Organisation Code"}
      </Typography>
      <Box sx={{ display: "flex", alignItems: "flex-end" }} mb={1}>
        <Business sx={{ color: "action.active", mr: 1, my: 0.5 }} />
        {organisationDetailsViewRenderer()}
      </Box>
      <Button sx={{ mt: 4 }} variant="contained" onClick={onHandleGoNext}>
        Next
      </Button>
      <Button
        sx={{ mt: 2 }}
        size={"small"}
        variant={"text"}
        onClick={() => handleGoBack(RegisterStepEnum.REGISTER_ORG_DETAILS)}
      >
        Back
      </Button>
    </>
  );
};

export default RegisterOrgDetails;
