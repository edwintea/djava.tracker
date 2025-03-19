import api from "../Api";
import { INewsletter, newsletterStore } from "../../stores/NewsletterStore";
import { uiStore } from "../../stores/UIStore";

export const createNewsletter = async (data: INewsletter) => {
  const result = await api.post("/newsletters", data);

  if (result.status === 201 && result.data) {
    uiStore.setSnackbarShow(
      true,
      "Created newsletter successfully!",
      "success"
    );
  }
};

export const getNewsletterPageable = async (page: number, size: number) => {
  const result = await api.get<INewsletter[]>(
    `/newsletters/specific?page=${page}&size=${size}`
  );

  if (result.status === 200 && result.data) {
    newsletterStore.setNewsletters(result.data);

    return result;
  }
  return null;
};
