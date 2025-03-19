import { makeAutoObservable } from "mobx";

export interface INewsletter {
  id?: string;
  subject: string;
  body: string;
  createdDate: string;
  organisation: string;
}

class NewsletterStore {
  constructor() {
    makeAutoObservable(this);
  }

  newsletters: INewsletter[] = [];
  setNewsletters = (newsletter: INewsletter[]) => {
    this.newsletters = newsletter;
  };
}

export const newsletterStore = new NewsletterStore();
