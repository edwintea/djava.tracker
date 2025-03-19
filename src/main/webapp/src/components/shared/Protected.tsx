import { Navigate } from "react-router-dom";
import { authenticationStore } from "../../stores/AuthenticationStore";
import { observer } from "mobx-react";

const Protected = (props: { children: JSX.Element }) => {
  const { children } = props;
  const { isAuthenticated } = authenticationStore;

  if (isAuthenticated !== null && !isAuthenticated) {
    return <Navigate to="/" replace />;
  } else {
    return isAuthenticated ? children : <></>;
  }
};

export default observer(Protected);
