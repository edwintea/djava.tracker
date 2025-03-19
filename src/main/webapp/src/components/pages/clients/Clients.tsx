import React from "react";
import { observer } from "mobx-react";
import {profileStore} from "../../../stores/ProfileStore";
import PrivilegedAssignment from "./PrivilegedAssignment";
import UserAssignment from "./UserAssignment";


const Assignments = () => {
  const {isPrivileged} = profileStore

  return isPrivileged ? <PrivilegedAssignment /> : <UserAssignment />
};

export default observer(Assignments);
