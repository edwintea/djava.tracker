import {observer} from "mobx-react";
import Content from "../../layouts/Content";
import {Typography} from "@mui/material";
import {lightText} from "../../../constants/Colours";
import React from "react";

const UserAssignment = () => {
  return <Content>
    <Typography
      variant={"h6"}
      color={lightText}
      fontSize={"medium"}
      fontWeight={"bolder"}
      letterSpacing={2}
      textTransform={"uppercase"}
      flex={1}
    >
      Assignments
    </Typography>
  </Content>
}

export default observer(UserAssignment)
