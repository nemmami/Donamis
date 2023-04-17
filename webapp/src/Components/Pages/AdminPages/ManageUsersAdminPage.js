import {getSessionObject} from "../../../utils/session";
import {Redirect} from "../../Router/Router";

let pageDiv2 = document.querySelector("#page2");
let tableWaiting, tableDeclined;
let errorStatus;

/**
 * Render the ManageUsersAdminPage
 */
const ManageUsersAdminPage = () => {
  pageDiv2.innerHTML = `<br>
  <div id="usersWaiting"></div>
  <div id="usersDeclined"></div>`;

  getAllUserWaiting();

  getAllUserDeclined();

  pageDiv2.querySelectorAll("button").forEach((button) => { // redirect
    button.addEventListener("click", (e) => {
      Redirect(e.target.dataset.uri);
    });
  });
};

async function getAllUserWaiting() {
  try {
    const options = {
      method: "GET", // *GET, POST, PUT, DELETE, etc.
      headers: {
        Authorization: getSessionObject("user").token,
      },
    };
    const response = await fetch(`/api/users/waiting`, options);
    if (!response.ok) {
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }
    const usersWaiting = await response.json();
    //console.log("tous les users en attente", usersWaiting);
    let pageUsersWaiting = document.querySelector("#usersWaiting");

    if (usersWaiting.length == 0) {
      tableWaiting = `
      <div id="titre">
        <br>
        <h2>Tous les membres en attente </h2>
        <br>
      </div>
      <h2> Il n'y a aucun membre en attente
      <div id="messageErreur" class="alert alert-danger">
      <a id="message">  </a>`;
    } else {
      tableWaiting = `
    <div id="titre">
      <br>
      <h2>Membres en attente  </h2>
      <br>
    </div>
    <br>
    <div id="tableAdmin">
      <table  class="table">
        <thead>
          <tr id="infoTete">
            <th class="col">Pseudo</th>  
            <th class="col">Nom</th>
            <th class="col">Prénom</th>
            <th class="col">Etat Inscription</th>
            <th class="col">Futur Admin</th>
            <th class="col">Raison refus</th>
            <th class="col"></th>
          </tr>
        </thead>
        <tbody>`;

      usersWaiting.forEach((users) => {
        tableWaiting += `
          <tr data-pseudo="${users.pseudo}">
            <td class="col">${users.pseudo}</td>
            <td class="col">${users.nom}</td>
            <td class="col">${users.prenom}</td>
            <td class="col">
              <select id="selectOptionEtat" class="form-select" aria-label="Default select example">
                <option selected>Etat</option>
                <option value="1">Confirmé</option>
                <option value="2">Refusé</option>
              </select>
            <td class="col">
              <input type="checkbox" id="isAdmin">
            </td>
            <td class="col">
            <textarea class="form-control" placeholder="Ajoutez la raison du refus" id="raisonRefus" type="text"></textarea>
            </td>
            <td class="col">
              <button id="btnEnvoyer" class="btn btn-primary saveWaiting" type"button">Valider</button>
            </td>
          </tr>`;
      });

      tableWaiting += `
        </tbody>
      </table>
      <div id="messageErreur" class="alert alert-danger">
      <a id="message">  </a>
    </div>
    </div>`;
    }

    pageUsersWaiting.innerHTML = tableWaiting;

    document.getElementById("messageErreur").style.display = "none";

    const btnEnvoyer = document.querySelectorAll(".saveWaiting");
    btnEnvoyer.forEach((btn) => {
      btn.addEventListener("click", () => {
        let row = btn.closest("tr");
        let pseudo = row.dataset.pseudo;
        //console.log(pseudo);
        let adminCheck = row.querySelector("#isAdmin");
        //console.log(adminCheck.checked);

        let etat = row.querySelector("#selectOptionEtat").value;

        if (etat == 1 && adminCheck.checked) {
          setEtatInscriptionConfirmee(pseudo);
          setAdmin(pseudo);
        } else if (etat == 1 && !adminCheck.checked) {
          setEtatInscriptionConfirmee(pseudo);
        } else if (etat == 2) {
          setEtatInscriptionRefusee(pseudo);
        }
      });
    });
  } catch (error) {
    console.error("ManageUsersPage for waiting users::error: ", error);
  }
}

async function getAllUserDeclined() {
  try {
    const options = {
      method: "GET", // *GET, POST, PUT, DELETE, etc.
      headers: {
        Authorization: getSessionObject("user").token,
      },
    };
    const response = await fetch(`/api/users/declined`, options);

    if (!response.ok) {
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }
    const usersDeclined = await response.json();
    //console.log("toutes les users refusée", usersDeclined);
    let pageUsersDeclined = document.querySelector("#usersDeclined");

    if (usersDeclined.length == 0) {
      tableDeclined = `<br><br>
      <div id="titre">
        <br>
        <h2>Tous les membres refusés </h2>
        <br>
      </div>
      <h2> Il n'y a aucun membre refusés`;
    } else {
      tableDeclined = `
    <br><br>
    <div id="titre">
      <br>
      <h2>Tous les membres refusés </h2>
      <br>
    </div>
    <br>
    <div id="tableAdmin">
    <table  class="table">
      <thead>
        <tr id="infoTete">
          <th class="col">Pseudo</th>  
          <th class="col">Nom</th>
          <th class="col">Prénom</th>
          <th class="col">Etat Inscription</th>
          <th class="col">Futur Admin</th>
          <th class="col"></th>
        </tr>
      </thead>
      <tbody>`;

      usersDeclined.forEach((users) => {
        tableDeclined += `
        <tr data-pseudo="${users.pseudo}">
          <td class="col">${users.pseudo}</td>
          <td class="col">${users.nom}</td>
          <td class="col">${users.prenom}</td>
          <td class="col">
            <select id="selectOptionEtat" class="form-select" aria-label="Default select example">
              <option selected>Etat</option>
              <option value="1">Confirmé</option>
            </select>
          <td class="col">
            <input type="checkbox" id="isAdmin">
          </td>
          <td class="col">
            <button id="btnEnvoyer" class="btn btn-primary saveDeclined" type"button">Valider</button>
          </td>
        </tr>`;
      });

      tableDeclined += `
        </tbody>
      </table>
    </div>`;
    }

    pageUsersDeclined.innerHTML = tableDeclined;

    const btnEnvoyer = document.querySelectorAll(".saveDeclined");
    btnEnvoyer.forEach((btn) => {
      btn.addEventListener("click", () => {
        let row = btn.closest("tr");
        let pseudo = row.dataset.pseudo;
        //console.log(pseudo);
        let adminCheck = row.querySelector("#isAdmin");
        //console.log(adminCheck.checked);

        let etat = row.querySelector("#selectOptionEtat").value;
        if (etat == 1 && adminCheck.checked) {
          setEtatInscriptionConfirmee(pseudo);
          setAdmin(pseudo);
        } else if (etat == 1 && !adminCheck.checked) {
          setEtatInscriptionConfirmee(pseudo);
        }
      });
    });
  } catch (error) {
    console.error("ManageUsersPage for declined users::error: ", error);
  }
}

async function setEtatInscriptionConfirmee(pseudo) {
  try {
    const options = {
      method: "POST", // *GET, POST, PUT, DELETE, etc.
      headers: {
        Authorization: getSessionObject("user").token,
      },
    };
    const response = await fetch(
        `/api/users/setEtat/confirmed/${pseudo}`,
        options
    );

    if (!response.ok) {
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }
    
    Redirect("/manageUsers");
  } catch (error) {
    console.error(
        "ManageUsersPage setEtatInscriptionConfirmee::error: ",
        error
    );
  }
}

async function setEtatInscriptionRefusee(pseudo) {
  let raisonRefus = document.getElementById("raisonRefus");
  //console.log(raisonRefus.value);
  try {
    const options = {
      method: "POST", // *GET, POST, PUT, DELETE, etc.
      body: JSON.stringify({
        raisonRefus: raisonRefus.value,
      }),
      headers: {
        "Content-Type": "application/json",
        Authorization: getSessionObject("user").token,
      },
    };
    const response = await fetch(
        `/api/users/setEtat/declined/${pseudo}`,
        options
    );

    if (!response.ok) {
      errorStatus = response.status;
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }
    
    Redirect("/manageUsers");
  } catch (error) {
    let message = document.getElementById("message");
    document.getElementById("messageErreur").style.display = "block";

    if (errorStatus == 400) {
      message.innerText = "Veuillez remplir la raison du refus";
    }

    console.error("ManageUsersPage setEtatInscriptionRefusee::error: ", error);
  }
}

async function setAdmin(pseudo) {
  try {
    const options = {
      method: "POST", // *GET, POST, PUT, DELETE, etc.
      headers: {
        Authorization: getSessionObject("user").token,
      },
    };
    const response = await fetch(`/api/users/setAdmin/${pseudo}`, options);

    if (!response.ok) {
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }
    
  } catch (error) {
    console.error("ManageUsersPage setAdmin::error: ", error);
  }
}

export default ManageUsersAdminPage;
