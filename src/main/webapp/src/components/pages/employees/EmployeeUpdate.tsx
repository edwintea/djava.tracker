import {
  Button,
  Chip,
  FormControl,
  FormControlLabel,
  Grid,
  InputLabel,
  MenuItem,
  Radio,
  Select,
  SelectChangeEvent,
  TextField,
  Typography,
} from "@mui/material";
import { lightText } from "../../../constants/Colours";
import React, { ChangeEvent, useEffect, useState } from "react";
import Content from "../../layouts/Content";
import { useNavigate, useParams } from "react-router-dom";
import { employeeStore, EmployeeTypes } from "../../../stores/EmployeeStore";
import Tile from "../../layouts/Tile";
import dayjs, { Dayjs } from "dayjs";
import TextFieldRounded from "../../shared/TextFieldRounded";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { DesktopDatePicker } from "@mui/x-date-pickers";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { departmentStore } from "../../../stores/DepartmentStore";
import { positionStore } from "../../../stores/PositionStore";
import { updateEmployee } from "../../../api/service/EmployeeApi";
import AssignmentTile from "./AssignmentTile";
import AssignmentAddTile from "./AssignmentAddTile";
import { employeeAssignmentStore } from "../../../stores/EmployeeAssignmentStore";
import { getEmployeeAssignments } from "../../../api/service/EmployeeAssignmentApi";
import QualificationTile from "./employeeUpdate/QualificationTile";

const EmployeeUpdate = () => {
  const { employeeId } = useParams();
  const { employees } = employeeStore;
  const { employeeAssignments } = employeeAssignmentStore;
  const { departments } = departmentStore;
  const { positions } = positionStore;
  const navigate = useNavigate();
  const employee = employees.find((e) => e.id === employeeId);

  const [fullName, setFullName] = useState("");
  const [sinceDate, setSinceDate] = useState<Dayjs | null>(dayjs());
  const [type, setType] = useState("");
  const [position, setPosition] = useState("");
  const [positionName, setPositionName] = useState("");
  const [department, setDepartment] = useState("");
  const [departmentName, setDepartmentName] = useState("");
  const [reportsTo, setReportsTo] = useState("");
  const [isAssignable, setIsAssignable] = useState(false);
  const [disabledUpdate, setDisabledUpdate] = useState(true);

  useEffect(() => {
    if (employee) {
      const {
        fullName,
        sinceDate: joinedDate,
        type,
        department,
        position,
        reportsTo,
        isAssignable,
      } = employee;
      setFullName(fullName);
      setSinceDate(
        joinedDate && joinedDate.length > 0 ? dayjs(joinedDate) : dayjs()
      );
      setType(type);
      setDepartment(department);
      setPosition(position);
      setReportsTo(reportsTo);
      setIsAssignable(isAssignable);

      getEmployeeAssignments(employee.user);
    }
  }, [employee]);

  useEffect(() => {
    if (employee) {
      const {
        sinceDate: sD,
        type: t,
        position: p,
        department: d,
        reportsTo: rT,
        isAssignable: iA,
      } = employee;

      if (t !== type || rT !== reportsTo) {
        setDisabledUpdate(false);
      } else if (sinceDate !== null && sD !== dayjs(sinceDate).toISOString()) {
        setDisabledUpdate(false);
      } else if (d !== department) {
        setDisabledUpdate(false);
      } else if (p !== position && position.length > 0) {
        setDisabledUpdate(false);
      } else if (iA !== isAssignable) {
        setDisabledUpdate(false);
      } else {
        setDisabledUpdate(true);
      }
    }
  }, [
    employee,
    sinceDate,
    type,
    position,
    department,
    reportsTo,
    isAssignable,
  ]);

  const handleTypeChange = (event: SelectChangeEvent) => {
    setType(event.target.value as string);
  };

  const handleDepartmentChange = (event: SelectChangeEvent) => {
    const departmentId = event.target.value as string;
    setDepartment(departmentId);
    setPosition("");

    if (departments.length > 0) {
      const department = departments.find((dep) => dep.id === departmentId);
      setDepartmentName(department ? department.name : "");
    }
  };

  const handlePositionChange = (event: SelectChangeEvent) => {
    const positionId = event.target.value as string;
    setPosition(event.target.value as string);

    if (positions.length > 0) {
      const position = positions.find((pos) => pos.id === positionId);
      setPositionName(position ? position.name : "");
    }
  };

  const handleReportsToChange = (event: SelectChangeEvent) => {
    setReportsTo(event.target.value as string);
  };

  const handleRadioChange = (event: ChangeEvent<HTMLInputElement>) => {
    setIsAssignable(event.target.value === "true");
  };

  const handleUpdateEmployee = () => {
    if (employee) {
      const { id, fullName, organisation, isAssigned, user } = employee;
      updateEmployee({
        id,
        isAssignable,
        type,
        sinceDate: sinceDate ? sinceDate.toISOString() : dayjs().toISOString(),
        position,
        department,
        reportsTo,
        fullName,
        organisation,
        isAssigned,
        user,
        positionName,
        departmentName,
      }).then(() => navigate(-1));
    }
  };

  return (
    <Content>
      {employee && (
        <>
          <Typography
            variant={"h6"}
            color={lightText}
            fontSize={"medium"}
            fontWeight={"bolder"}
            letterSpacing={2}
            textTransform={"uppercase"}
          >
            Employee
          </Typography>
          <Grid container spacing={5} mt={0.5}>
            <Grid item xs={12} md={6}>
              <Tile title={"Personal Details"} titleLeftAlign>
                <Grid container spacing={3} mt={0.2}>
                  <Grid item xs={12} md={8}>
                    <TextFieldRounded
                      id={"edit-fullName"}
                      label={"full name"}
                      size={"small"}
                      variant={"outlined"}
                      value={fullName}
                      setValue={setFullName}
                      disabled
                      fullWidth
                    />
                  </Grid>
                </Grid>
              </Tile>
            </Grid>
            <Grid item xs={12} md={6}>
              <Tile title={"HR Details"} titleLeftAlign>
                <Grid container spacing={3} mt={0.2}>
                  <Grid item xs={12} md={12}>
                    <LocalizationProvider dateAdapter={AdapterDayjs}>
                      <DesktopDatePicker
                        label="joined date"
                        inputFormat="DD/MM/YYYY"
                        value={sinceDate}
                        onChange={(e) => setSinceDate(e)}
                        renderInput={(params) => (
                          <TextField
                            id="edit-sinceDate"
                            label="joined date"
                            sx={{
                              "& .MuiOutlinedInput-root": {
                                "& fieldset": {
                                  borderRadius: 8,
                                },
                              },
                            }}
                            variant={"outlined"}
                            size={"small"}
                            fullWidth
                            {...params}
                          />
                        )}
                      />
                    </LocalizationProvider>
                  </Grid>
                  <Grid item xs={12} md={12}>
                    <FormControl fullWidth>
                      <InputLabel id="label-edit-type" size={"small"}>
                        type
                      </InputLabel>
                      <Select
                        labelId="label-edit-type"
                        id="edit-type"
                        value={type}
                        label="type"
                        size={"small"}
                        onChange={handleTypeChange}
                        sx={{
                          borderRadius: 8,
                        }}
                        fullWidth
                      >
                        {EmployeeTypes.map((type) => (
                          <MenuItem value={type.value}>{type.name}</MenuItem>
                        ))}
                      </Select>
                    </FormControl>
                  </Grid>
                  <Grid item xs={12} md={6}>
                    <FormControl fullWidth>
                      <InputLabel id="label-edit-department" size={"small"}>
                        department
                      </InputLabel>
                      <Select
                        labelId="label-edit-department"
                        id="edit-department"
                        value={department}
                        label="department"
                        size={"small"}
                        onChange={handleDepartmentChange}
                        sx={{
                          borderRadius: 8,
                        }}
                        fullWidth
                      >
                        {departments.map((department) => (
                          <MenuItem value={department.id}>
                            {department.name}
                          </MenuItem>
                        ))}
                      </Select>
                    </FormControl>
                  </Grid>
                  <Grid item xs={12} md={6}>
                    <FormControl fullWidth>
                      <InputLabel id="label-edit-position" size={"small"}>
                        position
                      </InputLabel>
                      <Select
                        labelId="label-edit-position"
                        id="edit-position"
                        value={position}
                        label="position"
                        size={"small"}
                        onChange={handlePositionChange}
                        sx={{
                          borderRadius: 8,
                        }}
                        fullWidth
                        disabled={!(department && department.length > 0)}
                      >
                        {positions
                          .filter(
                            (position) => position.department === department
                          )
                          .map((position) => (
                            <MenuItem value={position.id}>
                              {position.name}
                            </MenuItem>
                          ))}
                      </Select>
                    </FormControl>
                  </Grid>
                  <Grid item xs={12} md={12}>
                    <FormControl fullWidth>
                      <InputLabel id="label-edit-position" size={"small"}>
                        reports to
                      </InputLabel>
                      <Select
                        labelId="label-reports-to"
                        id="edit-reports-to"
                        value={reportsTo}
                        label="reports to"
                        size={"small"}
                        onChange={handleReportsToChange}
                        sx={{
                          borderRadius: 8,
                        }}
                        fullWidth
                      >
                        {employees
                          .filter((employee) => employee.id !== employeeId)
                          .map((employee) => (
                            <MenuItem value={employee.id}>
                              {employee.fullName}
                            </MenuItem>
                          ))}
                      </Select>
                    </FormControl>
                  </Grid>
                  <Grid
                    item
                    xs={12}
                    md={12}
                    display={"flex"}
                    flexDirection={"row"}
                    alignItems={"center"}
                  >
                    <Typography mr={2}>Assignable?</Typography>
                    <FormControlLabel
                      value="true"
                      control={
                        <Radio
                          defaultChecked
                          checked={isAssignable}
                          onChange={handleRadioChange}
                          size={"small"}
                        />
                      }
                      label="Yes"
                    />
                    <FormControlLabel
                      value="false"
                      control={
                        <Radio
                          checked={!isAssignable}
                          onChange={handleRadioChange}
                          size={"small"}
                        />
                      }
                      label="No"
                    />
                  </Grid>
                  <Grid
                    item
                    xs={12}
                    md={12}
                    display={"flex"}
                    justifyContent={"flex-end"}
                  >
                    <Button
                      sx={{
                        borderRadius: 8,
                      }}
                      variant={"contained"}
                      disabled={disabledUpdate}
                      onClick={handleUpdateEmployee}
                    >
                      Update
                    </Button>
                  </Grid>
                </Grid>
              </Tile>
            </Grid>
            {employee.isAssignable && (
              <Grid item xs={12} md={12}>
                <Tile title={"Assignment Details"} titleLeftAlign>
                  <Grid container spacing={3} mt={0.2}>
                    {employeeAssignments.map((employeeAssignment) => {
                      const {
                        clientName,
                        assignmentType,
                        startDate,
                        toDate,
                        main,
                      } = employeeAssignment;
                      return (
                        <Grid item xs={12} md={4} height={210}>
                          <AssignmentTile
                            clientName={clientName}
                            assignmentType={assignmentType}
                            fromDate={startDate}
                            toDate={toDate}
                            main={main}
                          />
                        </Grid>
                      );
                    })}
                    <Grid item xs={12} md={4} height={210}>
                      <AssignmentAddTile employee={employee} />
                    </Grid>
                  </Grid>
                </Tile>
              </Grid>
            )}
            <Grid item xs={12} md={12}>
              <Tile title={"Skills"} titleLeftAlign>
                <Grid container mt={0.2}>
                  <Grid item xs={12} md={6} mt={2}>
                    <Grid container spacing={1}>
                      <Grid item xs={12} md={12}>
                        <Typography
                          variant={"caption"}
                          color={lightText}
                          fontSize={"small"}
                          fontWeight={"bold"}
                        >
                          Languages
                        </Typography>
                      </Grid>
                      <Grid item xs={12} md={12}>
                        {[0, 1, 2, 3].map((pos) => {
                          return pos % 2 === 0 ? (
                            <Chip label={"React"} sx={{ m: 1 }} />
                          ) : (
                            <Chip label={"A very long name"} sx={{ m: 0.5 }} />
                          );
                        })}
                      </Grid>
                    </Grid>
                  </Grid>
                  <Grid item xs={12} md={6} mt={2}>
                    <Grid container spacing={1}>
                      <Grid item xs={12} md={12}>
                        <Typography
                          variant={"caption"}
                          color={lightText}
                          fontSize={"small"}
                          fontWeight={"bold"}
                        >
                          Frameworks
                        </Typography>
                      </Grid>
                      <Grid item xs={12} md={12}>
                        {[0, 1, 2, 3, 4, 5, 6, 7].map((pos) => {
                          return pos % 2 === 0 ? (
                            <Chip label={"React"} sx={{ m: 1 }} />
                          ) : (
                            <Chip label={"A very long name"} sx={{ m: 0.5 }} />
                          );
                        })}
                      </Grid>
                    </Grid>
                  </Grid>
                  <Grid item xs={12} md={6} mt={2}>
                    <Grid container spacing={1}>
                      <Grid item xs={12} md={12}>
                        <Typography
                          variant={"caption"}
                          color={lightText}
                          fontSize={"small"}
                          fontWeight={"bold"}
                        >
                          Methodology
                        </Typography>
                      </Grid>
                      <Grid item xs={12} md={12}>
                        {[0, 1, 2, 3, 4, 5, 6].map((pos) => {
                          return pos % 2 === 0 ? (
                            <Chip label={"React"} sx={{ m: 1 }} />
                          ) : (
                            <Chip label={"A very long name"} sx={{ m: 0.5 }} />
                          );
                        })}
                      </Grid>
                    </Grid>
                  </Grid>
                </Grid>
              </Tile>
            </Grid>
            <Grid item xs={12} md={12}>
              <Tile title={"Qualifications"} titleLeftAlign>
                <Grid container spacing={3} mt={0.2}>
                  <Grid item xs={12} md={4} height={160}>
                    <QualificationTile
                      title={"Bachelors in Software Engineering"}
                      year={"2022"}
                      institution={"Taylor's University"}
                      level={"Bachelors"}
                    />
                  </Grid>
                </Grid>
              </Tile>
            </Grid>
          </Grid>
        </>
      )}
    </Content>
  );
};

export default EmployeeUpdate;
