import { Route, Routes as RoutesDOM } from "react-router-dom";
import { pages } from "../../constants/AppRoutes";
import Protected from "./Protected";

const Routes = () => {
  return (
    <RoutesDOM>
      {pages.map((page) =>
        page.secure ? (
          <Route
            key={`${Math.random()}`}
            {...page}
            element={<Protected>{page.element}</Protected>}
          />
        ) : (
          <Route key={`${Math.random()}`} {...page} />
        )
      )}
    </RoutesDOM>
  );
};

export default Routes;
