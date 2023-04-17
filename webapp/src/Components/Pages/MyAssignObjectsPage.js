import {Redirect} from "../Router/Router";
import {getSessionObject} from "../../utils/session";

const pageDiv2 = document.querySelector("#page2");
let tableAssignObjects;

/**
 * Render the MyAssignObjectsPage
 */
const MyAssignObjectsPage = () => {
  pageDiv2.innerHTML = "";
  getAllObjectFromUser(getSessionObject("user").id);

  page.querySelectorAll("button").forEach((button) => { //redirect
    button.addEventListener("click", (e) => {
      Redirect(e.target.dataset.uri);
    });
  });
};

async function getAllObjectFromUser(id) {
  try {
    const options = {
      method: "GET", // *GET, POST, PUT, DELETE, etc.
      headers: {
        Authorization: getSessionObject("user").token,
      },
    };

    const response = await fetch(
        `/api/chosenInterest/getAllChosenInterestMember/${id}`,
        options
    );
    if (!response.ok) {
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }
    const myObjects = await response.json();
    //console.log("objets attribué", myObjects);

    function importAll(r) {
      let images = {};
      r.keys().map((item, index) => {
        images[item.replace("./", "")] = r(item);
      });
      return images;
    }

    const images = importAll(
        require.context("../../../../photo", false, /\.(png|jpe?g)$/)
    );

    if (myObjects.length == 0) {
      tableAssignObjects = `
      <br>
      <div id="titre">
        <br>
        <h2>Objects reçus  </h2>
        <br>
      </div>
      <h2> Vous n'avez reçu aucun objet <br></h2>
`;
    } else {
      tableAssignObjects = `
    <br>
    <div id="titre">
      <br>
      <h2>Objects reçus  </h2>
      <br>
    </div>
    <div id="tableUsers" class="table-responsive mt-3">
      <table class="table">
        <thead>
          <tr id="infoTete">
            <th class="col">Titre</th>
            <th class="col">Description</th>
            <th class="col">Photo</th>
            <th class="col">Membre</th> 
            <th class="col">Etat</th>          
          </tr>
        </thead>
        <tbody>`;

      myObjects.forEach((object) => {
        if (object.etat_transaction != "Venu") {
          return
        }
        if (object.photo === null) {
          object.photo = "default.jpg";
        }
        tableAssignObjects += `
          <tr data-id="${object.id}">
            <td class="col">${object.titre}</td>
            <td class="col">${object.description}</td>
            <td class="col"><img id="imageHome" src=${
            images[object.photo]
        } href="#"> </td> 
            <td class="col">${object.membre_offreur}</td>
            <td class="col">${object.etat_transaction}</td>
          </tr>`;
      });

      tableAssignObjects += `
        </tbody>
      </table>
    </div>`;
    }

    pageDiv2.innerHTML = tableAssignObjects;
  } catch (error) {
    console.error("MyAssignObjectsPage::error: ", error);
  }
}

export default MyAssignObjectsPage;
