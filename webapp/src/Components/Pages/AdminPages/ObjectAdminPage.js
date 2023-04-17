import ObjectLibrary from "../../../Domain/ObjectLibrary";
import { getSessionObject } from "../../../utils/session";

const myObjectsLibrary = new ObjectLibrary();

/**
 * Render the ObjectAdminPage
 */
const ObjectAdminPage = async () => {
  const pageDiv2 = document.querySelector("#page2");
  pageDiv2.innerHTML += `<br>`;

  pageDiv2.innerHTML = await myObjectsLibrary.getObject(
    getSessionObject("object").id
  );
};

export default ObjectAdminPage;
