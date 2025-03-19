import {observer} from "mobx-react";
import {Typography} from "@mui/material";
import {lightText} from "../../../constants/Colours";
import Content from "../../layouts/Content";
import React from "react";

const UserDashboard = () => {
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
      Dashboard
    </Typography>
  </Content>
}

export default observer(UserDashboard)
