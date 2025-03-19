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
import { getClientMinimal } from "../../../api/service/ClientApi";
import { observer } from "mobx-react";
import { clientStore } from "../../../stores/ClientStore";
import { createAssignment } from "../../../api/service/AssignmentApi";
import { profileStore } from "../../../stores/ProfileStore";

const AddAssignment = () => {
  const { clientMinimal } = clientStore;
  const [client, setClient] = useState("");
  const [type, setType] = useState("");
  const [clientDescription, setClientDescription] = useState("");

  useEffect(() => {
    getClientMinimal();
  }, []);

  const handleClientChange = (event: SelectChangeEvent) => {
    setClient(event.target.value as string);
  };

  const handleTypeChange = (event: SelectChangeEvent) => {
    setType(event.target.value as string);
  };

  const handleCreateAssignment = () => {
    createAssignment({
      client,
      assignmentType: type,
      description: clientDescription,
      organisation: profileStore.profile.employee.organisation,
    }).then(reset);
  };

  const reset = () => {
    setClient("");
    setType("");
    setClientDescription("");
  };

  return (
    <Tile title={"Add Assignment"}>
      <FormControl
        sx={{
          mt: 2,
          ml: 5,
          mr: 5,
        }}
      >
        <InputLabel id="demo-simple-select-label" size={"small"}>
          client
        </InputLabel>
        <Select
          labelId="demo-simple-select-label"
          id="demo-simple-select"
          value={client}
          label="client"
          size={"small"}
          onChange={handleClientChange}
          sx={{
            borderRadius: 8,
          }}
        >
          {clientMinimal.map((client) => (
            <MenuItem value={client.id}>{client.name}</MenuItem>
          ))}
        </Select>
      </FormControl>
      <FormControl
        sx={{
          mt: 1,
          ml: 5,
          mr: 5,
        }}
      >
        <InputLabel id="demo-simple-select-label" size={"small"}>
          type
        </InputLabel>
        <Select
          labelId="demo-simple-select-label"
          id="demo-simple-select"
          value={type}
          label="type"
          size={"small"}
          onChange={handleTypeChange}
          sx={{
            borderRadius: 8,
          }}
        >
          <MenuItem value={"OUTSOURCE"}>Outsource</MenuItem>
          <MenuItem value={"PROJECT"}>Project</MenuItem>
          <MenuItem value={"IN_HOUSE"}>In House</MenuItem>
        </Select>
      </FormControl>
      <TextFieldRounded
        id="clientAddress"
        label="description"
        size={"small"}
        variant="outlined"
        value={clientDescription}
        setValue={setClientDescription}
        multiline
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
        onClick={handleCreateAssignment}
      >
        Add
      </Button>
    </Tile>
  );
};

export default observer(AddAssignment);
