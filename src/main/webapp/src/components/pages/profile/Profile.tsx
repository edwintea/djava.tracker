import { Box, Typography } from "@mui/material";
import { lightText } from "../../../constants/Colours";
import Button from "@mui/material/Button";
import { AddCircle } from "@mui/icons-material";
import React from "react";
import Content from "../../layouts/Content";

const Profile = () => {
  return (
    <Content>
      <Box display={"flex"} flexDirection={"row"} alignItems={"center"}>
        <Typography
          variant={"h6"}
          color={lightText}
          fontSize={"medium"}
          fontWeight={"bolder"}
          letterSpacing={2}
          textTransform={"uppercase"}
          flex={1}
        >
          Profile
        </Typography>
        <Button
          sx={{ backgroundColor: "transparent", color: "transparent" }}
          startIcon={<AddCircle />}
        >
          Add
        </Button>
      </Box>
    </Content>
  );
};

export default Profile;
