import {profileStore} from "../../../stores/ProfileStore";
import PrivilegedDashboard from "./PrivilegedDashboard";
import UserDashboard from "./UserDashboard";

const Dashboard = () => {
  const { isPrivileged } = profileStore

  return isPrivileged ? <PrivilegedDashboard /> : <UserDashboard />
};

export default Dashboard;
