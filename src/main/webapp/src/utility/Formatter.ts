import dayjs from "dayjs";

export const prettifyDate = (date: string) => {
  const dayjsDate = dayjs(date);
  return dayjsDate
    .format(`D MMMM YYYY`)
    .replace(
      `${dayjsDate.date()}`,
      `${dayjsDate.date()}${dayPostfix(dayjsDate.date())}`
    );
};

const dayPostfix = (date: number) => {
  const dateNo = date;
  const dateNoLast = Number(date.toString().slice(-1));

  if ((dateNo > 0 && dateNo < 11) || dateNo > 19) {
    switch (dateNoLast) {
      case 1:
        return "st";
      case 2:
        return "nd";
      case 3:
        return "rd";
      default:
        return "th";
    }
  }
  return "th";
};

export const assignmentTypeFormat = (type: string) => {
  return capitalWords(type.replace("_", " "));
};

export const capitalWords = (text: string) => {
  return text
    .toLowerCase()
    .split(" ")
    .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
    .join(" ");
};

export const capitalFrontOnly = (text: string) => {
  return text.length > 0
    ? text
        .charAt(0)
        .toUpperCase()
        .concat(text.slice(1, text.length).toLowerCase())
    : text;
};
