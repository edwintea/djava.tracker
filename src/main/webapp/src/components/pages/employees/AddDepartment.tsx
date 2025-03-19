import Tile from "../../layouts/Tile";
import { Button } from "@mui/material";
import { AddCircle } from "@mui/icons-material";
import * as React from "react";
import { useState } from "react";
import TextFieldRounded from "../../shared/TextFieldRounded";
import { createDepartment } from "../../../api/service/DepartmentApi";
import { profileStore } from "../../../stores/ProfileStore";

const AddDepartment = () => {
  const [departmentName, setDepartmentName] = useState("");

  const handleCreateDepartment = () => {
    createDepartment({
      name: departmentName,
      organisation: profileStore.profile.employee.organisation,
    }).then(() => resetForm());
  };

  const resetForm = () => {
    setDepartmentName("");
  };

  return (
    <Tile title={"Add Department"}>
      <TextFieldRounded
        id="departmentName"
        label="department name"
        size={"small"}
        variant="outlined"
        value={departmentName}
        setValue={setDepartmentName}
        sx={{
          mt: 2,
          ml: 5,
          mr: 5,
        }}
      />
      <Button
        size={"small"}
        variant={"contained"}
        startIcon={<AddCircle />}
        sx={{
          mt: 2,
          ml: 5,
          mr: 5,
          borderRadius: 8,
        }}
        onClick={handleCreateDepartment}
      >
        Add
      </Button>
    </Tile>
  );
};

export default AddDepartment;
