import { makeAutoObservable } from "mobx";

type Severity = "success" | "warning" | "error";

class UIStore {
  constructor() {
    makeAutoObservable(this);
  }

  loadingShow: boolean = false;
  loadingText: string = "";
  setLoading = (show: boolean, text: string = "") => {
    this.loadingShow = show;
    this.loadingText = text;
  };

  snackbarShow: boolean = false;
  snackbarText: string = "";
  snackbarSeverity: Severity = "success";
  setSnackbarShow = (
    show: boolean,
    text: string = "",
    severity: Severity = "success"
  ) => {
    this.snackbarShow = show;
    this.snackbarText = text;
    this.snackbarSeverity = show ? severity : this.snackbarSeverity;
  };
}

export const uiStore = new UIStore();
