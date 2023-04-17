const STORE_NAME = "user";

/**
 * Get the Object that is in the localStorage or sessionStorage under the storeName key
 * @param {string} storeName
 * @returns
 */
const getSessionObject = (storeName) => {
  if (sessionStorage.getItem(storeName)) {
    const retrievedObject = sessionStorage.getItem(storeName);
    if (!retrievedObject) return;
    return JSON.parse(retrievedObject);
  } else {
    const retrievedObject = localStorage.getItem(storeName);
    if (!retrievedObject) return;
    return JSON.parse(retrievedObject);
  }
};

/**
 * Set the object in the localStorage or sessionStorage under the storeName key
 * @param {string} storeName
 * @param {Object} object
 */
const setSessionObject = (storeName, object, rememberMe) => {
  if (!rememberMe) {
    console.log("set sessionStorage");
    const storageValue = JSON.stringify(object);
    sessionStorage.setItem(storeName, storageValue);
  } else {
    console.log("set localStorage");
    const storageValue = JSON.stringify(object);
    localStorage.setItem(storeName, storageValue);
  }
};

/**
 * Remove the object in the localStorage or sessionStorage under the storeName key
 * @param {String} storeName
 */
const removeSessionObject = (storeName) => {
  if (sessionStorage.getItem(storeName)) {
    console.log("delete sessionStorage");
    sessionStorage.removeItem(storeName);
  } else {
    console.log("delete localStorage");
    localStorage.removeItem(storeName);
  }
};

export { getSessionObject, setSessionObject, removeSessionObject };
