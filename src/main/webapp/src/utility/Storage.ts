export const setStorage = (key: string, data: string) => {
  if (window) {
    window.localStorage.setItem(key, data);
  }
};

export const getStorage = (key: string) => {
  if (window) {
    return window.localStorage.getItem(key);
  }
  return null;
};

export const clearStorage = (key: string) => {
  if (window) {
    window.localStorage.removeItem(key);
  }
};

export const clearAllStorage = () => {
  if (window) {
    window.localStorage.clear();
  }
};
