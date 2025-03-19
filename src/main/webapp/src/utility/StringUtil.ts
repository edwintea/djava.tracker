export const isEmpty = (text: string) => {
  if (text) {
    return !(text.length > 0);
  } else {
    return true;
  }
};
