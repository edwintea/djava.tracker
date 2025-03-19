import {
  Box,
  Button,
  ButtonBase,
  FormControl,
  InputLabel,
  lighten,
  MenuItem,
  Paper,
  Select,
  SelectChangeEvent,
  TextField,
  Typography,
} from "@mui/material";
import { lightText } from "../../../constants/Colours";
import { AddCircle, ArrowRightRounded } from "@mui/icons-material";
import React, {
  Dispatch,
  ReactNode,
  SetStateAction,
  useEffect,
  useState,
} from "react";
import { observer } from "mobx-react";
import { clientStore } from "../../../stores/ClientStore";
import dayjs, { Dayjs } from "dayjs";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { DesktopDatePicker } from "@mui/x-date-pickers";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import TextFieldRounded from "../../shared/TextFieldRounded";
import { assignmentTypeFormat, prettifyDate } from "../../../utility/Formatter";
import {
  createEmployeeAssignment,
  getEmployeeAssignments,
} from "../../../api/service/EmployeeAssignmentApi";
import { IEmployee } from "../../../stores/EmployeeStore";
import { getAssignmentMinimal } from "../../../api/service/AssignmentApi";
import { assignmentStore } from "../../../stores/AssignmentStore";
import { employeeAssignmentStore } from "../../../stores/EmployeeAssignmentStore";

enum AddAssignmentStep {
  PRE_STEP = "PRE_STEP",
  SET_MAIN_ASSIGNMENT = "SET_MAIN_ASSIGNMENT",
  SET_DATE = "SET_START",
  SET_DESCRIPTION = "SET_DESCRIPTION",
  SET_SUMMARY = "SET_SUMMARY",
}

const AssignmentPaperBase = ({ children }: { children: ReactNode }) => {
  return (
    <Paper
      elevation={4}
      sx={{
        display: "flex",
        flexDirection: "column",
        justifyContent: "space-evenly",
        height: "100%",
        w: "100%",
        borderRadius: 8,
        p: 2,
      }}
    >
      {children}
    </Paper>
  );
};

const AssignmentPreStep = ({
  setAddStep,
}: {
  setAddStep: Dispatch<SetStateAction<AddAssignmentStep>>;
}) => {
  return (
    <ButtonBase
      sx={{
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        height: "100%",
        width: "100%",
        borderRadius: 8,
        border: 1,
        borderStyle: "dashed",
        borderColor: lighten(lightText, 0.4),
        backgroundColor: lighten(lightText, 0.94),
      }}
      onClick={() => setAddStep(AddAssignmentStep.SET_MAIN_ASSIGNMENT)}
    >
      <AddCircle />
      <Typography ml={1}>Add Assignment</Typography>
    </ButtonBase>
  );
};

const AssignmentSetMainAssignment = observer(
  ({
    mainAssignment,
    setClient,
    setType,
    setMainAssignment,
    setAddStep,
  }: {
    mainAssignment: string;
    setClient: Dispatch<SetStateAction<string>>;
    setType: Dispatch<SetStateAction<string>>;
    setMainAssignment: Dispatch<SetStateAction<string>>;
    setAddStep: Dispatch<SetStateAction<AddAssignmentStep>>;
  }) => {
    const { employeeAssignments } = employeeAssignmentStore;
    const { assignmentMinimal } = assignmentStore;
    const alreadyAssignedAssignments = employeeAssignments.map(
      (assignment) => assignment.assignment
    );
    const availableAssignments = assignmentMinimal.filter(
      (assignment) => !alreadyAssignedAssignments.includes(assignment.id ?? "")
    );

    useEffect(() => {
      getAssignmentMinimal();
    }, []);

    const handleMainAssignmentChange = (event: SelectChangeEvent) => {
      const assignmentId = event.target.value as string;
      const assignment = assignmentMinimal.find(
        (assign) => assign.id === assignmentId
      );
      if (assignment) {
        setType(assignment.assignmentType);
        setClient(assignment.client);
        setMainAssignment(assignmentId);
      }
    };

    return (
      <AssignmentPaperBase>
        <Typography textAlign={"center"}>Assignment</Typography>
        {availableAssignments.length > 0 ? (
          <FormControl>
            <InputLabel id="demo-simple-select-label" size={"small"}>
              assignment
            </InputLabel>
            <Select
              labelId="demo-simple-select-label"
              id="demo-simple-select"
              value={mainAssignment}
              label="assignment"
              size={"small"}
              onChange={handleMainAssignmentChange}
              sx={{
                borderRadius: 8,
              }}
            >
              {availableAssignments.map((assignment) => (
                <MenuItem value={assignment.id}>{`${
                  assignment.clientName
                } - ${assignmentTypeFormat(
                  assignment.assignmentType
                )}`}</MenuItem>
              ))}
            </Select>
          </FormControl>
        ) : (
          <Typography
            variant={"caption"}
            fontSize={"small"}
            color={lightText}
            textAlign={"center"}
          >
            No more available assignments!
          </Typography>
        )}
        <Box sx={{ ml: "auto" }}>
          <Button
            size={"small"}
            variant={"outlined"}
            sx={{
              borderRadius: 8,
              ml: "auto",
            }}
            color={"error"}
            onClick={() => setAddStep(AddAssignmentStep.PRE_STEP)}
          >
            Cancel
          </Button>
          {availableAssignments.length > 0 && (
            <Button
              size={"small"}
              variant={"contained"}
              sx={{ borderRadius: 8, ml: 1 }}
              onClick={() => setAddStep(AddAssignmentStep.SET_DATE)}
            >
              Next
            </Button>
          )}
        </Box>
      </AssignmentPaperBase>
    );
  }
);

const AssignmentSetDate = observer(
  ({
    fromDate,
    toDate,
    setFromDate,
    setToDate,
    setAddStep,
  }: {
    fromDate: Dayjs | null;
    toDate: Dayjs | null;
    setFromDate: Dispatch<SetStateAction<Dayjs | null>>;
    setToDate: Dispatch<SetStateAction<Dayjs | null>>;
    setAddStep: Dispatch<SetStateAction<AddAssignmentStep>>;
  }) => {
    return (
      <AssignmentPaperBase>
        <Typography textAlign={"center"}>Assignment Duration</Typography>
        <Box sx={{ display: "flex", flexDirection: "row" }}>
          <LocalizationProvider dateAdapter={AdapterDayjs}>
            <DesktopDatePicker
              label="start date"
              inputFormat="DD/MM/YYYY"
              value={fromDate}
              onChange={(e) => setFromDate(e)}
              renderInput={(params) => (
                <TextField
                  id="startDate"
                  label="start date"
                  sx={{
                    "& .MuiOutlinedInput-root": {
                      "& fieldset": {
                        borderRadius: 8,
                      },
                    },
                    mr: 1,
                  }}
                  variant={"outlined"}
                  size={"small"}
                  {...params}
                />
              )}
            />
          </LocalizationProvider>
          <LocalizationProvider dateAdapter={AdapterDayjs}>
            <DesktopDatePicker
              label="to date"
              inputFormat="DD/MM/YYYY"
              value={toDate}
              onChange={(e) => setToDate(e)}
              renderInput={(params) => (
                <TextField
                  id="toDate"
                  label="to date"
                  sx={{
                    "& .MuiOutlinedInput-root": {
                      "& fieldset": {
                        borderRadius: 8,
                      },
                    },
                    ml: 1,
                  }}
                  variant={"outlined"}
                  size={"small"}
                  {...params}
                />
              )}
            />
          </LocalizationProvider>
        </Box>
        <Box sx={{ ml: "auto" }}>
          <Button
            size={"small"}
            variant={"outlined"}
            sx={{
              borderRadius: 8,
              ml: "auto",
            }}
            color={"error"}
            onClick={() => setAddStep(AddAssignmentStep.PRE_STEP)}
          >
            Cancel
          </Button>

          <Button
            size={"small"}
            variant={"contained"}
            sx={{ borderRadius: 8, ml: 1 }}
            onClick={() => setAddStep(AddAssignmentStep.SET_MAIN_ASSIGNMENT)}
          >
            Back
          </Button>
          <Button
            size={"small"}
            variant={"contained"}
            sx={{ borderRadius: 8, ml: 1 }}
            onClick={() => setAddStep(AddAssignmentStep.SET_DESCRIPTION)}
          >
            Next
          </Button>
        </Box>
      </AssignmentPaperBase>
    );
  }
);

const AssignmentSetDescription = observer(
  ({
    description,
    setDescription,
    setAddStep,
  }: {
    description: string;
    setDescription: Dispatch<SetStateAction<string>>;
    setAddStep: Dispatch<SetStateAction<AddAssignmentStep>>;
  }) => {
    return (
      <AssignmentPaperBase>
        <Typography textAlign={"center"}>Description</Typography>
        <TextFieldRounded
          id={"add-description"}
          label={"description"}
          size={"small"}
          variant={"outlined"}
          value={description}
          setValue={setDescription}
          multiline
        />
        <Box sx={{ ml: "auto" }}>
          <Button
            size={"small"}
            variant={"outlined"}
            sx={{
              borderRadius: 8,
              ml: "auto",
            }}
            color={"error"}
            onClick={() => setAddStep(AddAssignmentStep.PRE_STEP)}
          >
            Cancel
          </Button>
          <Button
            size={"small"}
            variant={"contained"}
            sx={{ borderRadius: 8, ml: 1 }}
            onClick={() => setAddStep(AddAssignmentStep.SET_DATE)}
          >
            Back
          </Button>
          <Button
            size={"small"}
            variant={"contained"}
            sx={{ borderRadius: 8, ml: 1 }}
            onClick={() => setAddStep(AddAssignmentStep.SET_SUMMARY)}
          >
            Next
          </Button>
        </Box>
      </AssignmentPaperBase>
    );
  }
);

const AssignmentSummary = observer(
  ({
    client,
    assignmentType,
    fromDate,
    toDate,
    description,
    mainAssignment,
    setAddStep,
    employee,
    reset,
  }: {
    client: string;
    assignmentType: string;
    fromDate: Dayjs | null;
    toDate: Dayjs | null;
    description: string;
    mainAssignment: string;
    setAddStep: Dispatch<SetStateAction<AddAssignmentStep>>;
    employee: IEmployee;
    reset: () => void;
  }) => {
    const { clients } = clientStore;
    const { employeeAssignments } = employeeAssignmentStore;
    const clientDetails = clients.find((cli) => cli.id === client);

    const onHandleCreateAssignment = () => {
      if (clientDetails) {
        createEmployeeAssignment({
          client,
          startDate: fromDate ? fromDate.toISOString() : dayjs().toISOString(),
          toDate: toDate ? toDate.toISOString() : dayjs().toISOString(),
          assignmentType,
          active: true,
          main: employeeAssignments.length === 0,
          clientName: clientDetails.name,
          description,
          assignment: mainAssignment,
          user: employee.user,
          organisation: employee.organisation,
        }).then((result) => {
          if (result && result.status === 201) {
            reset();
            setAddStep(AddAssignmentStep.PRE_STEP);
            getEmployeeAssignments(employee.user);
          }
        });
      }
    };

    return (
      <AssignmentPaperBase>
        <Typography fontWeight={"bold"}>
          {clientDetails ? clientDetails.name : "Client not found"}
        </Typography>
        <Typography>{assignmentTypeFormat(assignmentType)}</Typography>
        <Typography
          variant={"caption"}
          color={lightText}
          sx={{
            display: "flex",
            alignItems: "center",
          }}
        >
          {`${prettifyDate(
            fromDate ? fromDate.toISOString() : dayjs().toISOString()
          )}`}
          <ArrowRightRounded />
          {`${prettifyDate(
            toDate ? toDate.toISOString() : dayjs().toISOString()
          )}`}
        </Typography>
        <Box sx={{ ml: "auto" }}>
          <Button
            size={"small"}
            variant={"contained"}
            sx={{ borderRadius: 8, ml: 1 }}
            onClick={() => setAddStep(AddAssignmentStep.SET_DESCRIPTION)}
          >
            Back
          </Button>
          <Button
            size={"small"}
            variant={"contained"}
            sx={{ borderRadius: 8, ml: 1 }}
            onClick={onHandleCreateAssignment}
          >
            Add Assignment
          </Button>
        </Box>
      </AssignmentPaperBase>
    );
  }
);

const AssignmentAddTile = ({ employee }: { employee: IEmployee }) => {
  const { setEmployeeAssignments } = employeeAssignmentStore;
  const [mainAssignment, setMainAssignment] = useState("");
  const [client, setClient] = useState("");
  const [fromDate, setFromDate] = useState<Dayjs | null>(dayjs());
  const [toDate, setToDate] = useState<Dayjs | null>(dayjs());
  const [type, setType] = useState("");
  const [clientDescription, setClientDescription] = useState("");
  const [addStep, setAddStep] = useState<AddAssignmentStep>(
    AddAssignmentStep.PRE_STEP
  );

  useEffect(() => {
    return setEmployeeAssignments([]);
  }, []);

  const reset = () => {
    setClient("");
    setFromDate(dayjs());
    setToDate(dayjs());
    setType("");
    setMainAssignment("");
    setClientDescription("");
  };

  const renderAssignmentStep = (step: AddAssignmentStep) => {
    switch (step) {
      case AddAssignmentStep.PRE_STEP:
        return <AssignmentPreStep setAddStep={setAddStep} />;
      case AddAssignmentStep.SET_MAIN_ASSIGNMENT:
        return (
          <AssignmentSetMainAssignment
            setAddStep={setAddStep}
            mainAssignment={mainAssignment}
            setClient={setClient}
            setType={setType}
            setMainAssignment={setMainAssignment}
          />
        );
      case AddAssignmentStep.SET_DATE:
        return (
          <AssignmentSetDate
            setAddStep={setAddStep}
            fromDate={fromDate}
            setFromDate={setFromDate}
            toDate={toDate}
            setToDate={setToDate}
          />
        );
      case AddAssignmentStep.SET_DESCRIPTION:
        return (
          <AssignmentSetDescription
            setAddStep={setAddStep}
            description={clientDescription}
            setDescription={setClientDescription}
          />
        );
      case AddAssignmentStep.SET_SUMMARY:
        return (
          <AssignmentSummary
            client={client}
            assignmentType={type}
            fromDate={fromDate}
            toDate={toDate}
            description={clientDescription}
            mainAssignment={mainAssignment}
            setAddStep={setAddStep}
            employee={employee}
            reset={reset}
          />
        );
    }
  };

  return <>{renderAssignmentStep(addStep)}</>;
};

export default observer(AssignmentAddTile);
