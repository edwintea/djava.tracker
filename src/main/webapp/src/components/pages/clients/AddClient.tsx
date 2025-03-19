import Tile from "../../layouts/Tile";
import TextFieldRounded from "../../shared/TextFieldRounded";
import { Button, TextField } from "@mui/material";
import { AddCircle } from "@mui/icons-material";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import * as React from "react";
import { useState } from "react";
import { createClient } from "../../../api/service/ClientApi";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { DesktopDatePicker } from "@mui/x-date-pickers";
import dayjs, { Dayjs } from "dayjs";
import { profileStore } from "../../../stores/ProfileStore";

const AddClient = () => {
  const [clientName, setClientName] = useState("");
  const [clientSince, setClientSince] = useState<Dayjs | null>(dayjs());
  const [clientAddress, setClientAddress] = useState("");

  const handleCreateClient = () => {
    createClient({
      name: clientName,
      sinceDate: clientSince
        ? clientSince.toISOString()
        : dayjs().toISOString(),
      address: clientAddress,
      organisation: profileStore.profile.employee.organisation,
    }).then(reset);
  };

  const reset = () => {
    setClientName("");
    setClientSince(dayjs());
    setClientAddress("");
  };

  return (
    <Tile title={"Add Client"}>
      <TextFieldRounded
        id="clientName"
        label="client name"
        size={"small"}
        variant="outlined"
        value={clientName}
        setValue={setClientName}
        sx={{
          mt: 2,
          ml: 5,
          mr: 5,
        }}
      />
      <LocalizationProvider dateAdapter={AdapterDayjs}>
        <DesktopDatePicker
          label="client since"
          inputFormat="DD/MM/YYYY"
          value={clientSince}
          onChange={(e) => setClientSince(e)}
          renderInput={(params) => (
            <TextField
              id="clientSince"
              label="client since"
              sx={{
                "& .MuiOutlinedInput-root": {
                  "& fieldset": {
                    borderRadius: 8,
                  },
                },
                mt: 1,
                ml: 5,
                mr: 5,
              }}
              variant={"outlined"}
              size={"small"}
              {...params}
            />
          )}
        />
      </LocalizationProvider>
      <TextFieldRounded
        id="clientAddress"
        label="client address"
        size={"small"}
        variant="outlined"
        value={clientAddress}
        setValue={setClientAddress}
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
        onClick={handleCreateClient}
      >
        Add
      </Button>
    </Tile>
  );
};

export default AddClient;
