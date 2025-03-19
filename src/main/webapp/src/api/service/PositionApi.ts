import {
  IPosition,
  IPositionEmployeeCount,
  positionStore,
} from "../../stores/PositionStore";
import api from "../Api";
import { uiStore } from "../../stores/UIStore";

export const createPosition = async (data: IPosition) => {
  const result = await api.post("/positions", data);

  if (result.status === 201 && result.data) {
    uiStore.setSnackbarShow(true, "Created position successfully!", "success");
  }
};

export const getPositions = async () => {
  const result = await api.get<IPositionEmployeeCount[]>("/positions/specific");

  if (result.status === 200 && result.data) {
    positionStore.setPositions(result.data);
    return result;
  }
  return null;
};
