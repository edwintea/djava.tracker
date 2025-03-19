import {observer} from "mobx-react";
import {Typography} from "@mui/material";
import {lightText} from "../../../constants/Colours";
import React from "react";
import Content from "../../layouts/Content";
import {profileStore} from "../../../stores/ProfileStore";

const LeaveRequest = () => {
  const {isPrivileged} = profileStore

  return !isPrivileged ? <Content>
            <Typography
              variant={"h6"}
              color={lightText}
              fontSize={"medium"}
              fontWeight={"bolder"}
              letterSpacing={2}
              textTransform={"uppercase"}
              flex={1}
            >
              Leave Requests
            </Typography>
        </Content> : <></>
}

export default observer(LeaveRequest)
