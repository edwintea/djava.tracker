import { authenticationStore } from "../../stores/AuthenticationStore";
import { useLocation, useNavigate } from "react-router-dom";
import { useEffect } from "react";
import { clearStorage, getStorage } from "../../utility/Storage";
import api from "../../api/Api";
import { observer } from "mobx-react";
import { getProfile } from "../../api/service/ProfileApi";
import { getOrganisationBase } from "../../api/service/OrganisationApi";
import { pages } from "../../constants/AppRoutes";

const AuthHelper = () => {
  const { isAuthenticated, setIsAuthenticated } = authenticationStore;

  const location = useLocation();
  const navigate = useNavigate();

  const preApiFetch = () => {
    getOrganisationBase();
    switch (location.pathname) {
      case "/":
        navigate("/dashboard");
        break;
      default:
        break;
    }
  };

  useEffect(() => {
    if (isAuthenticated === null) {
      const token = getStorage("token");

      if (token) {
        api.setHeader("Authorization", `Bearer ${token}`);
        getProfile().then((result) => {
          if (result.status === 200) {
            preApiFetch();
            setIsAuthenticated(true);
          }
          if (result.status === 401) {
            api.deleteHeader("Authorization");
            clearStorage("token");
            setIsAuthenticated(false);
          }
        });
      } else {
        setIsAuthenticated(false);
      }
    }
    if (isAuthenticated !== null && !isAuthenticated) {
      setIsAuthenticated(false);
      const currentPath = location.pathname;
      const currentPage = pages.find((page) => page.path === currentPath);
      if (currentPage && currentPage.secure) {
        navigate("/");
      }
    }
  }, [isAuthenticated]);
  return <></>;
};

export default observer(AuthHelper);
