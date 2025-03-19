import { create } from "apisauce";
import { uiStore } from "../stores/UIStore";

const api = create({
  baseURL: "http://localhost:8080/api",
});

api.addRequestTransform((t) => {
  uiStore.setLoading(true);
});

api.addResponseTransform((t) => {
  uiStore.setLoading(false);
});

export default api;
