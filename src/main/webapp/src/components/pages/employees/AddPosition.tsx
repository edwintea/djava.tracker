import Tile from "../../layouts/Tile";
import TextFieldRounded from "../../shared/TextFieldRounded";
import {
  Button,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  SelectChangeEvent,
} from "@mui/material";
import { AddCircle } from "@mui/icons-material";
import * as React from "react";
import { useEffect, useState } from "react";
import { getDepartments } from "../../../api/service/DepartmentApi";
import { observer } from "mobx-react";
import { departmentStore } from "../../../stores/DepartmentStore";
import { createPosition } from "../../../api/service/PositionApi";
import { profileStore } from "../../../stores/ProfileStore";

const AddPosition = () => {
  const { departments } = departmentStore;

  const [department, setDepartment] = useState("");
  const [positionName, setPositionName] = useState("");

  useEffect(() => {
    getDepartments();
  }, []);

  const handleChange = (event: SelectChangeEvent) => {
    setDepartment(event.target.value as string);
  };

  const handleCreatePosition = () => {
    createPosition({
      name: positionName,
      department: department,
      organisation: profileStore.profile.employee.organisation,
    }).then(reset);
  };

  const reset = () => {
    setDepartment("");
    setPositionName("");
  };

  return (
    <Tile title={"Add Position"}>
      <FormControl
        sx={{
          mt: 2,
          ml: 5,
          mr: 5,
        }}
      >
        <InputLabel id="demo-simple-select-label" size={"small"}>
          department
        </InputLabel>
        <Select
          labelId="demo-simple-select-label"
          id="demo-simple-select"
          value={department}
          label="department"
          size={"small"}
          onChange={handleChange}
          sx={{
            borderRadius: 8,
          }}
        >
          {departments.map((department) => (
            <MenuItem value={department.id}>{department.name}</MenuItem>
          ))}
        </Select>
      </FormControl>
      <TextFieldRounded
        id="positionName"
        label="position name"
        size={"small"}
        variant="outlined"
        value={positionName}
        setValue={setPositionName}
        sx={{
          mt: 1,
          ml: 5,
          mr: 5,
        }}
      />
      <Button
        size={"small"}
        variant={"contained"}
        startIcon={<AddCircle />}
        sx={{
          mt: 4,
          ml: 5,
          mr: 5,
          borderRadius: 8,
        }}
        onClick={handleCreatePosition}
      >
        Add
      </Button>
    </Tile>
  );
};

export default observer(AddPosition);
