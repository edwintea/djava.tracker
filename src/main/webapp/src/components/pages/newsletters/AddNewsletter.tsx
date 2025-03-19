import Tile from "../../layouts/Tile";
import { Button } from "@mui/material";
import { AddCircle } from "@mui/icons-material";
import * as React from "react";
import { useState } from "react";
import TextFieldRounded from "../../shared/TextFieldRounded";
import { createNewsletter } from "../../../api/service/NewsletterApi";
import dayjs from "dayjs";
import { profileStore } from "../../../stores/ProfileStore";

const AddNewsletter = () => {
  const [newsletterSubject, setNewsletterSubject] = useState("");
  const [newsletterBody, setNewsletterBody] = useState("");

  const handleCreateNewsletter = () => {
    createNewsletter({
      subject: newsletterSubject,
      body: newsletterBody,
      createdDate: dayjs().toISOString(),
      organisation: profileStore.profile.employee.organisation,
    }).then(reset);
  };

  const reset = () => {
    setNewsletterSubject("");
    setNewsletterBody("");
  };

  return (
    <Tile title={"Add Newsletter"}>
      <TextFieldRounded
        id="newsletterSubject"
        label="newsletter subject"
        size={"small"}
        variant="outlined"
        value={newsletterSubject}
        setValue={setNewsletterSubject}
        sx={{
          mt: 2,
          ml: 5,
          mr: 5,
        }}
      />
      <TextFieldRounded
        id="newsletterBody"
        label="newsletter body"
        size={"small"}
        variant="outlined"
        value={newsletterBody}
        setValue={setNewsletterBody}
        sx={{
          mt: 1,
          ml: 5,
          mr: 5,
        }}
        multiline
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
        onClick={handleCreateNewsletter}
      >
        Add
      </Button>
    </Tile>
  );
};

export default AddNewsletter;
