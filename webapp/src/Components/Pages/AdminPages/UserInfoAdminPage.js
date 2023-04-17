import { getSessionObject } from "../../../utils/session";
import vImage from "../../../img/iconeV.png";
import xImage from "../../../img/iconeX.png";

let tableObjectUserSelected;
var admin;

/**
 * Render the UserInfoAdminPage
 */
const UserInfoAdminPage = async () => {
  const pageDiv2 = document.querySelector("#page2");
  let nbrObjectOfferts = await getAmountOfOffertObjects(
    getSessionObject("membreSelectedAdmin").id
  );

  let nbrObjectDonnes = await getAmountOfGivenObjects(
    getSessionObject("membreSelectedAdmin").id
  );

  let nbrObjectReçus = await getAmountOfReceivedObjects(
    getSessionObject("membreSelectedAdmin").id
  );

  let nbrInterestObjects = await getAllInterestForUserObject(
    getSessionObject("membreSelectedAdmin").id
  );

  let nbrPasVenuObjects = await getAmoutOfNeverCameObjects(
    getSessionObject("membreSelectedAdmin").id
  );

  if (getSessionObject("membreSelectedAdmin").admin == true) {
    admin = vImage;
  } else {
    admin = xImage;
  }

  pageDiv2.innerHTML = `<br><div id="titre"> 
  <br> 
  <h2> Membre selectioné : ${
    getSessionObject("membreSelectedAdmin").pseudo
  }</h2>
   <br>
   
   </div><br>
  <div id="tableAdmin">
    <table  class="table">
      <thead>
        <tr id="infoTete">
          <th class="col">Pseudo</th>  
          <th class="col">Nom</th>
          <th class="col">Prénom</th>
          <th class="col">Rôle admin</th>
          <th class="col">Nombre d'objets offerts</th>
          <th class="col">Nombre d’objets donnés</th>
          <th class="col">Nombre d’objets reçus</th>
          <th class="col">Nombre d’objets likés</th>
          <th class="col">Nombre d’objets qu'il/elle n'est jamais venu chercher</th>
        </tr>
      </thead>
      <tbody>
      <tr>
            <td class="col">${
              getSessionObject("membreSelectedAdmin").pseudo
            }</td>
            <td class="col">${getSessionObject("membreSelectedAdmin").nom}</td>
            <td class="col">${
              getSessionObject("membreSelectedAdmin").prenom
            }</td>
            <td class="col"><img id="vImage" src=${admin} href="#"></td>
            <td class="col"> ${nbrObjectOfferts} </td>
            <td class="col"> ${nbrObjectDonnes} </td>
            <td class="col"> ${nbrObjectReçus} </td>
            <td class="col"> ${nbrInterestObjects} </td>
            <td class="col"> ${nbrPasVenuObjects} </td> 
          </tr>
          </tbody>
      </table>
      </div>
      <br>

      <button type="submit" id="mesObjets" class="btn btn-primary offert"> 
      Voir les objects offerts de ${
        getSessionObject("membreSelectedAdmin").pseudo
      }
      </button>

      <button type="submit" id="mesObjets" class="btn btn-primary donnes">
      Voir les objects donnés de ${
        getSessionObject("membreSelectedAdmin").pseudo
      } 
      </button>

      <button type="submit" id="mesObjets" class="btn btn-primary recu">
      Voir les objects reçus de ${
        getSessionObject("membreSelectedAdmin").pseudo
      } 
      </button>
      <button type="submit" id="mesObjets" class="btn btn-primary pasVenu"> 
      Voir les objects que ${
        getSessionObject("membreSelectedAdmin").pseudo
      } n'est jamais venu cherchés
      </button>
      <div id="infoMembre"></div>`;

  document.querySelector(".offert").addEventListener("click", function () {
    getAllObjectFromUser(getSessionObject("membreSelectedAdmin").id);
  });
  document.querySelector(".donnes").addEventListener("click", function () {
    getGivenObjects(getSessionObject("membreSelectedAdmin").id);
  });
  document.querySelector(".recu").addEventListener("click", function () {
    getReceivedObjects(getSessionObject("membreSelectedAdmin").id);
  });
  document.querySelector(".pasVenu").addEventListener("click", function () {
    getNeverCameObjects(getSessionObject("membreSelectedAdmin").id);
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
    //console.log("objets offerts membre selectionné", myObjects);
    const pageInfo = document.getElementById("infoMembre");

    if (myObjects.length == 0) {
      tableObjectUserSelected = `<br>
         <h2> Il n'y a aucun object  <br></h2>
  `;
    } else {
      tableObjectUserSelected = `
      <br> <br>
      <div id="tableAdmin">
        <table class="table">
          <thead>
            <tr id="infoTete">
            <th class="col">Type</th>
            <th class="col">Titre</th>
            <th class="col">Description</th>
            <th class="col">Disponibilité</th>  
            <th class="col">Etat</th>
            <th class="col">Date d'ajout</th>
            <th class="col">Nombre de like</th>         
            </tr>
          </thead>
          <tbody>`;

      myObjects.forEach((object) => {
        tableObjectUserSelected += `
            <tr>
            <td class="col">${object.id_type}</td>
            <td class="col">${object.titre}</td>
            <td class="col">${object.description}</td>
            <td class="col">${object.plage_horaire}</td>
            <td class="col">${object.etat}</td>
            <td class="col">${object.date}</td>
            <td class="col">${object.nbr_interesse}</td>
            </tr>`;
      });

      tableObjectUserSelected += `
          </tbody>
        </table>
      </div>`;
    }

    pageInfo.innerHTML = tableObjectUserSelected;
  } catch (error) {
    console.error("UserInfoAdminPage::error: ", error);
  }
}

async function getAmountOfOffertObjects(id) {
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
    //console.log("nbr d'objets offerts", myObjects.length);

    return myObjects.length;
  } catch (error) {
    console.error("UserInfoAdminPage::error: ", error);
  }
}

async function getGivenObjects(id) {
  try {
    const options = {
      method: "GET", // *GET, POST, PUT, DELETE, etc.
      headers: {
        Authorization: getSessionObject("user").token,
      },
    };
    const response = await fetch(
      `/api/chosenInterest/getGivenObjects/${id}`,
      options
    );
    if (!response.ok) {
      throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
      );
    }
    const myObjects = await response.json();
    //console.log("objets donnés membre selectionné", myObjects);
    const pageInfo = document.getElementById("infoMembre");

    if (myObjects.length == 0) {
      tableObjectUserSelected = `<br>
         <h2> Il n'y a aucun object  <br></h2>
  `;
    } else {
      tableObjectUserSelected = `
      <br> <br>
      <div id="tableAdmin">
        <table class="table">
          <thead>
            <tr id="infoTete">
            <th class="col">Type</th>
            <th class="col">Titre</th>
            <th class="col">Description</th>
            <th class="col">Membre</th>
            <th class="col">Date d'ajout</th>
            <th class="col">Nombre de like</th>         
            </tr>
          </thead>
          <tbody>`;

      myObjects.forEach((object) => {
        tableObjectUserSelected += `
            <tr>
            <td class="col">${object.id_type}</td>
            <td class="col">${object.titre}</td>
            <td class="col">${object.description}</td>
            <td class="col">${object.membre_offreur}</td>
            <td class="col">${object.date}</td>
            <td class="col">${object.nbr_interesse}</td>
            </tr>`;
      });

      tableObjectUserSelected += `
          </tbody>
        </table>
      </div>`;
    }

    pageInfo.innerHTML = tableObjectUserSelected;
  } catch (error) {
    console.error("UserInfoAdminPage::error: ", error);
  }
}

async function getAmountOfGivenObjects(id) {
  try {
    const options = {
      method: "GET", // *GET, POST, PUT, DELETE, etc.
      headers: {
        Authorization: getSessionObject("user").token,
      },
    };
    const response = await fetch(
      `/api/chosenInterest/getGivenObjects/${id}`,
      options
    );
    if (!response.ok) {
      throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
      );
    }
    const myObjects = await response.json();
    //console.log("nbr d'objets donnés", myObjects.length);

    return myObjects.length;
  } catch (error) {
    console.error("UserInfoAdminPage::error: ", error);
  }
}

async function getReceivedObjects(id) {
  try {
    const options = {
      method: "GET", // *GET, POST, PUT, DELETE, etc.
      headers: {
        Authorization: getSessionObject("user").token,
      },
    };
    const response = await fetch(
      `/api/chosenInterest/getReceivedObjects/${id}`,
      options
    );
    if (!response.ok) {
      throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
      );
    }
    const myObjects = await response.json();
    //console.log("objets reçus membre selectionné", myObjects);
    const pageInfo = document.getElementById("infoMembre");

    if (myObjects.length == 0) {
      tableObjectUserSelected = `<br>
         <h2> Il n'y a aucun object  <br></h2>
  `;
    } else {
      tableObjectUserSelected = `
      <br> <br>
      <div id="tableAdmin">
        <table class="table">
          <thead>
            <tr id="infoTete">
            <th class="col">Type</th>
            <th class="col">Titre</th>
            <th class="col">Description</th>
            <th class="col">Membre</th>
            <th class="col">Date d'ajout</th>
            <th class="col">Nombre de like</th>         
            </tr>
          </thead>
          <tbody>`;

      myObjects.forEach((object) => {
        tableObjectUserSelected += `
            <tr>
            <td class="col">${object.id_type}</td>
            <td class="col">${object.titre}</td>
            <td class="col">${object.description}</td>
            <td class="col">${object.membre_offreur}</td>
            <td class="col">${object.date}</td>
            <td class="col">${object.nbr_interesse}</td>
            </tr>`;
      });

      tableObjectUserSelected += `
          </tbody>
        </table>
      </div>`;
    }

    pageInfo.innerHTML = tableObjectUserSelected;
  } catch (error) {
    console.error("UserInfoAdminPage::error: ", error);
  }
}

async function getAmountOfReceivedObjects(id) {
  try {
    const options = {
      method: "GET", // *GET, POST, PUT, DELETE, etc.
      headers: {
        Authorization: getSessionObject("user").token,
      },
    };
    const response = await fetch(
      `/api/chosenInterest/getReceivedObjects/${id}`,
      options
    );
    if (!response.ok) {
      throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
      );
    }
    const myObjects = await response.json();
    //console.log("nbr d'objets reçus", myObjects.length);

    return myObjects.length;
  } catch (error) {
    console.error("UserInfoAdminPage::error: ", error);
  }
}

async function getAllInterestForUserObject(id) {
  try {
    const options = {
      method: "GET", // *GET, POST, PUT, DELETE, etc.
      headers: {
        Authorization: getSessionObject("user").token,
      },
    };
    const response = await fetch(
      `/api/interest/allInterestMember/${id}`,
      options
    );
    if (!response.ok) {
      throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
      );
    }
    const interest = await response.json();
    //console.log("nbr de like", interest.length);

    return interest.length;
  } catch (error) {
    console.error("UserInfoAdminPage::error: ", error);
  }
}

async function getNeverCameObjects(id) {
  try {
    const options = {
      method: "GET", // *GET, POST, PUT, DELETE, etc.
      headers: {
        Authorization: getSessionObject("user").token,
      },
    };
    const response = await fetch(
      `/api/chosenInterest/getNeverCameObjects/${id}`,
      options
    );
    if (!response.ok) {
      throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
      );
    }
    const myObjects = await response.json();
    //console.log("objets jamais venu chercher par le membre selectionné", myObjects);
    const pageInfo = document.getElementById("infoMembre");

    if (myObjects.length == 0) {
      tableObjectUserSelected = `<br>
         <h2> Il n'y a aucun object  <br></h2>
  `;
    } else {
      tableObjectUserSelected = `
      <br> <br>
      <div id="tableAdmin">
        <table class="table">
          <thead>
            <tr id="infoTete">
            <th class="col">Type</th>
            <th class="col">Titre</th>
            <th class="col">Description</th>
            <th class="col">Membre</th>
            <th class="col">Date d'ajout</th>
            <th class="col">Nombre de like</th>         
            </tr>
          </thead>
          <tbody>`;

      myObjects.forEach((object) => {
        tableObjectUserSelected += `
            <tr>
            <td class="col">${object.id_type}</td>
            <td class="col">${object.titre}</td>
            <td class="col">${object.description}</td>
            <td class="col">${object.membre_offreur}</td>
            <td class="col">${object.date}</td>
            <td class="col">${object.nbr_interesse}</td>
            </tr>`;
      });

      tableObjectUserSelected += `
          </tbody>
        </table>
      </div>`;
    }

    pageInfo.innerHTML = tableObjectUserSelected;
  } catch (error) {
    console.error("UserInfoAdminPage::error: ", error);
  }
}

async function getAmoutOfNeverCameObjects(id) {
  try {
    const options = {
      method: "GET", // *GET, POST, PUT, DELETE, etc.
      headers: {
        Authorization: getSessionObject("user").token,
      },
    };
    const response = await fetch(
      `/api/chosenInterest/getNeverCameObjects/${id}`,
      options
    );
    if (!response.ok) {
      throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
      );
    }
    const myObjects = await response.json();
    //console.log("nbr d'objets pas venu chercher", myObjects.length);

    return myObjects.length;
  } catch (error) {
    console.error("UserInfoAdminPage::error: ", error);
  }
}



export default UserInfoAdminPage;
