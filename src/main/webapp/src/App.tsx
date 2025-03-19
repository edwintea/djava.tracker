import { BrowserRouter } from "react-router-dom";
import {
  Alert,
  Backdrop,
  CircularProgress,
  createTheme,
  Snackbar,
  ThemeProvider,
} from "@mui/material";
import { pastelRed, primary } from "./constants/Colours";
import TopBar from "./components/shared/TopNav";
import Routes from "./components/shared/Routes";
import { uiStore } from "./stores/UIStore";
import AuthHelper from "./components/authentication/AuthHelper";
import BottomNavigator from "./components/shared/BottomNavigator";
import { observer } from "mobx-react";

interface Props {
  /**
   * Injected by the documentation to work in an iframe.
   * You won't need it on your project.
   */
  window?: () => Window;
}

function App(props: Props) {
  const theme = createTheme({
    palette: {
      primary: {
        main: primary,
      },
      background: {
        default: "rgba(256,256,256,0.8)",
      },
      error: {
        main: pastelRed,
      },
    },
  });

  return (
    <BrowserRouter>
      <ThemeProvider theme={theme}>
        <TopBar props={props}>
          <Routes />
        </TopBar>
        <BottomNavigator />
        <Snackbar
          open={uiStore.snackbarShow}
          anchorOrigin={{ vertical: "top", horizontal: "left" }}
          autoHideDuration={3000}
          onClose={() => uiStore.setSnackbarShow(false)}
        >
          <Alert
            onClose={() => uiStore.setSnackbarShow(false)}
            severity={uiStore.snackbarSeverity}
            sx={{ width: "100%" }}
          >
            {uiStore.snackbarText}
          </Alert>
        </Snackbar>
        <Backdrop
          sx={{
            color: "#fff",
            zIndex: (theme) => theme.zIndex.drawer + 1,
            display: "flex",
            flexDirection: "column",
          }}
          open={uiStore.loadingShow}
          onClick={() => uiStore.setLoading(false)}
        >
          <CircularProgress color="inherit" sx={{ mb: 2 }} />
        </Backdrop>
        <AuthHelper />
      </ThemeProvider>
    </BrowserRouter>
  );
}

export default observer(App);
