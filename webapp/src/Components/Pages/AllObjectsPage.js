import { getSessionObject, setSessionObject } from "../../utils/session";
import ObjectLibrary from "../../Domain/ObjectLibrary";
import { Redirect } from "../Router/Router";

const pageDiv2 = document.querySelector("#page2");
const myObjectsLibrary = new ObjectLibrary();

/**
 * Render the AllObjectsPage
 */
const AllObjectsPage = async () => { 
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = "";

  pageDiv2.innerHTML = await myObjectsLibrary.getAllObjectsOffered();

  const btnEnvoyer = document.querySelectorAll(".moreInformation");

  btnEnvoyer.forEach((btn) => {
    btn.addEventListener("click", (e) => {
      const elementId = e.target.value;
      getObject(elementId);
    });
  });
};

async function getObject(id) {
  try {
    const options = {
      method: "GET", // *GET, POST, PUT, DELETE, etc.
      headers: {
        Authorization: getSessionObject("user").token,
      },
    };
    const response = await fetch(`/api/objects/${id}`, options);
    if (!response.ok) {
      throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
      );
    }
    const object = await response.json();
    //console.log("Objet selectionné", object);

    const btnEnvoyer = document.getElementById("btnEnvoyer");

    setSessionObject("object", object);
    btnEnvoyer.addEventListener("click", Redirect("/object"));
  } catch (error) {
    console.error("HomePage for last objects::error: ", error);
  }
}

export default AllObjectsPage;
