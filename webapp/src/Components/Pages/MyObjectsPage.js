import {Redirect} from "../Router/Router";
import {getSessionObject, setSessionObject} from "../../utils/session";

const pageDiv = document.querySelector("#page");
const pageDiv2 = document.querySelector("#page2");
let tableWaiting;
let objectS;

/**
 * Render the MyObjectsPage
 */
const MyObjectsPage = () => {
  //pageDiv.innerHTML = "";
  pageDiv2.innerHTML = "";
  getAllObjectFromUser(getSessionObject("user").id);

  page.querySelectorAll("button").forEach((button) => { // redirect
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
    const response = await fetch(`/api/objects/object/${id}`, options);
    if (!response.ok) {
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }
    const myObjects = await response.json();
    //console.log("mes objets", myObjects);

    function importAll(r) {
      let images = {};
      r.keys().map((item, index) => {
        images[item.replace("./", "")] = r(item);
      });
      return images;
    }

    const images = importAll( // permet d'importer toutes les images
        require.context("../../../../photo", false, /\.(png|jpe?g)$/)
    );

    if (myObjects.length == 0) {
      tableWaiting = `
      <br>
      <div id="titre">
        <br>
        <h2>Mes objets </h2>
        <br>
      </div>
      <h2> Vous n'avez encore aucun object <br></h2>
`;
    } else {
      tableWaiting = `
    <br>
    <div id="titre">
      <br>
      <h2>Mes objets  </h2>
      <br>
    </div>
    <div id="tableUsers" class="table-responsive mt-3">
      <table class="table">
        <thead>
          <tr id="infoTete">
            <th class="col">Titre</th> 
            <th class="col">Description</th>
            <th class="col">Disponibilité</th>
            <th class="col">Photo</th>
            <th class="col">Date</th>
            <th class="col">Etat</th>
            <th class="col"></th>  
            <th class="col"></th>
            <th class="col"></th>              
          </tr>
        </thead>
        <tbody>`;

      myObjects.forEach((object) => {
        if (object.photo === null) {
          object.photo = "default.jpg";
        }
        tableWaiting += `
          <tr data-id="${object.id}">
            <td class="col">${object.titre}</td>
            <td class="col">${object.plage_horaire}</td>
            <td class="col">${object.description}</td> 
            <td class="col">
              <img id="imageHome" src=${images[object.photo]} href="#">
            </td> 
            <td class="col">${object.date}</td>
            <td class="col">${object.etat}</td>`;
        if (object.etat == "Offert" || object.etat == "Interet marqué") { // En fonction de l'état de l'objet, on a des boutons différents
          tableWaiting += `
            <td class="col"><button id="btnAnnuler" class="btn btn-danger annuler" type="button">Annuler</button></td>
            <td class="col"><button id="btnInterest" class="btn btn-primary objetInterest" type="button">Voir membres interessés</button></td>
            <td class="col"><button id="btnUpdate" class="btn btn-secondary changeObject" type="button">Modifier l'object</button></td>
          </tr>`;
        } else if (object.etat == "Annulé") {
          tableWaiting += `
            <td class="col"><button id="btnReposter" class="btn btn-success reposter" type="button">Reposter</button></td>
            <td class="col"></td>
            <td class="col"><button id="btnUpdate" class="btn btn-secondary changeObject" type="button">Modifier l'object</button></td>
          </tr>`;
        } else if (object.etat == "Receveur Choisi") {
          tableWaiting += `
            <td class="col"></td>
            <td class="col"><button id="btnInterest" class="btn btn-primary objetInterest" type="button">Finaliser la transaction</button></td>
            <td class="col"></td>
          </tr>`;
        } else if (object.etat == "Donné") {
          tableWaiting += `
            <td class="col"></td>
            <td class="col"></td>
            <td class="col"></td>
          </tr>`;
        }
      });

      tableWaiting += `
        </tbody>
      </table>
    </div>`;
    }

    pageDiv2.innerHTML = tableWaiting;

    const annulerBtn = document.querySelectorAll(".annuler");
    annulerBtn.forEach((btn) => {
      btn.addEventListener("click", () => {
        let row = btn.closest("tr");
        let idObject = row.dataset.id;
        //console.log(idObject);
        annulerObject(idObject);
      });
    });

    const reposerBtn = document.querySelectorAll(".reposter");
    reposerBtn.forEach((btn) => {
      btn.addEventListener("click", async () => {
        let row = btn.closest("tr");
        let idObject = row.dataset.id;
        //console.log(idObject);
        reposertObject(idObject);
      });
    });

    const btnEnvoyer = document.querySelectorAll(".objetInterest");
    btnEnvoyer.forEach((btn) => {
      btn.addEventListener("click", () => {
        let row = btn.closest("tr");
        let id = row.dataset.id;
        //console.log(id);
        getObject(id);
      });
    });

    const btnUpdate = document.querySelectorAll(".changeObject");
    btnUpdate.forEach((btn) => {
      btn.addEventListener("click", (e) => {
        let row = btn.closest("tr");
        let id = row.dataset.id;
        //console.log(id);
        getObjectForUpdate(id);
      });
    });
  } catch (error) {
    console.error("MyObjectsPage::error: ", error);
  }
}

async function getObjectForUpdate(id) {
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

    setSessionObject("object", object);
    Redirect("/updatePage");
  } catch (error) {
    console.error("MyObjectsPage ::error: ", error);
  }
}

async function annulerObject(id_objet) {
  let id = id_objet;

  try {
    const options = {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: getSessionObject("user").token,
      },
    };
    const response = await fetch(`/api/objects/${id}`, options);

    if (!response.ok) {
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }
    const data = await response.json();
    //console.log(data);

    //refresh la page
    Redirect("/myObjects");
    //getAllObjectFromUser(getSessionObject("user").id);
  } catch (error) {
    console.error(error);
  }
}

async function reposertObject(id_objet) {
  let id = id_objet;

  try {
    const options = {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: getSessionObject("user").token,
      },
    };
    const response = await fetch(`/api/objects/reposter/${id}`, options);

    if (!response.ok) {
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }
    const data = await response.json();
    //console.log(data);

    Redirect("/myObjects");
    //getAllObjectFromUser(getSessionObject("user").id);
  } catch (error) {
    console.error(error);
  }
}

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
    objectS = object;
    //console.log("test", objectS)

    //console.log("ici c'est lobjet : ", object)
    setSessionObject("objectInteret", object);
    Redirect("/objectInterest");
    return object;

  } catch (error) {
    console.error("MyObjectsPage ::error: ", error);
  }
}

export default MyObjectsPage;
