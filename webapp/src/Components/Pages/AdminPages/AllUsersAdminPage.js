import { getSessionObject, setSessionObject } from "../../../utils/session";
import { Redirect } from "../../Router/Router";
import vImage from "../../../img/iconeV.png";
import xImage from "../../../img/iconeX.png";
import NotificationLibrary from "../../../Domain/NotificationLibrary";

let pageDiv2 = document.querySelector("#page2");
let tableConfirmed;
const myNotificationLibrary = new NotificationLibrary();
let membreEmpechement;

/**
 * Render the AllUsersAdminPage
 */
const AllUsersAdminPage = () => {
  pageDiv2.innerHTML = `
  <div id="usersConfirmed"></div>
  <div id="usersDeclined"></div>`;

  getAllUserConfirmed();

  pageDiv2.querySelectorAll("button").forEach((button) => { // redirect
    button.addEventListener("click", (e) => {
      Redirect(e.target.dataset.uri);
    });
  });
};

async function getAllUserConfirmed() {
  try {
    const options = {
      method: "GET", // *GET, POST, PUT, DELETE, etc.
      headers: {
        Authorization: getSessionObject("user").token,
      },
    };
    const response = await fetch(`/api/users/confirmed`, options);
    if (!response.ok) {
      throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
      );
    }
    const usersConfirmed = await response.json();
    //console.log("tous les users confirmé", usersConfirmed);
    let pageusersConfirmed = document.querySelector("#usersConfirmed");

    if (usersConfirmed.length == 0) {
      tableConfirmed = `
      <br>
      <div id="titre">
        <br>
        <h2>Tous les membres confirmés : </h2>
        <br>
      </div>
      <h2> Il n'y a aucun membre confirmé
      <div id="messageErreur" class="alert alert-danger">
        <a id="message"></a>
      </div>`;
    } else {
      tableConfirmed = `
    <br>
    <div id="titre">
      <br>
      <h2>Tous les membres confirmés : </h2>
      <br>
    </div>
    <br>
    <div id="tableUsers">
      <table  class="table">
        <thead>
          <tr id="infoTete">
            <th class="col">Pseudo</th>  
            <th class="col">Nom</th>
            <th class="col">Prénom</th>
            <th class="col">Rôle admin</th>
            <th class="col">Empêchement</th>
            <th class="col">Info sur le membre</th>
            <th class="col">Empêché de participer à la donnerie</th>
          </tr>
        </thead>
        <tbody>`;

      usersConfirmed.forEach((users) => {
        var admin;
        if (users.admin == true) {
          admin = vImage;
        } else {
          admin = xImage;
        }

        if (users.pseudo == getSessionObject("user").pseudo) { // on affiche pas le user connecté dans la liste
        } else {
          tableConfirmed += `
          <tr data-pseudo="${users.pseudo}">
            <td class="col">${users.pseudo}</td>
            <td class="col">${users.nom}</td>
            <td class="col">${users.prenom}</td>
            <td class="col"> <img id="vImage" src=${admin} href="#"> </td>`;

          if (users.etatInscription == "Empeché") {
            tableConfirmed += `<td class="col"><img id="vImage" src=${vImage} href="#"></td>`;
          } else {
            tableConfirmed += `<td class="col"><img id="xImage" src=${xImage} href="#"></td>`;
          }
          tableConfirmed += `
            <td class="col">
              <button id="btnEnvoyer" class="btn btn-primary plusInfo" type"button">Plus d'info</button>
             
            </td>`;
          if (users.etatInscription == "Empeché") {
            tableConfirmed += `<td class="col">
              <button id="btnReposter" class="btn btn-success confirmerParticipation" type"button">Confirmer la participation</button>
            </td>
            </tr>`;
          } else {
            tableConfirmed += `<td class="col">
            <button id="btnAnnuler" class="btn btn-danger annulerParticipation" type"button">Empêcher la participation</button>
          </td>
          </tr>`;
          }
        }
      });

      tableConfirmed += `
        </tbody>
      </table>
    </div>
    </div>`;
    }

    pageusersConfirmed.innerHTML = tableConfirmed;

    const btnEnvoyer = document.querySelectorAll(".plusInfo");
    btnEnvoyer.forEach((btn) => {
      btn.addEventListener("click", () => {
        let row = btn.closest("tr");
        let pseudo = row.dataset.pseudo;
        //console.log(pseudo);
        getOne(pseudo);
      });
    });

    const btnEmpecher = document.querySelectorAll(".annulerParticipation");
    btnEmpecher.forEach((btn) => {
      btn.addEventListener("click", async () => {
        let row = btn.closest("tr");
        let pseudo = row.dataset.pseudo;
        let membre = await getUserInfo(pseudo);
        annulerParticipation(membre.id); //set etat empeché
      });
    });

    const btnConfirmer = document.querySelectorAll(".confirmerParticipation");
    btnConfirmer.forEach((btn) => {
      btn.addEventListener("click",  () => {
        let row = btn.closest("tr");
        let pseudo = row.dataset.pseudo;
        setEtatInscriptionConfirmee(pseudo); //set etat confirmé
      });
    });
  } catch (error) {
    console.error("AllUsersAdminPage ::error: ", error);
  }
}

async function getOne(pseudo) {
  try {
    const options = {
      method: "GET", // *GET, POST, PUT, DELETE, etc.
      headers: {
        Authorization: getSessionObject("user").token,
      },
    };
    const response = await fetch(`/api/users/${pseudo}`, options);

    if (!response.ok) {
      throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
      );
    }
    const userSelected = await response.json();
    //console.log("membre selectionné", userSelected);
    setSessionObject("membreSelectedAdmin", userSelected);

    Redirect("/userInfo");
  } catch (error) {
    console.error("AllUsersAdminPage for declined users::error: ", error);
  }
}

async function getUserInfo(pseudo) {
  try {
    const options = {
      method: "GET", // *GET, POST, PUT, DELETE, etc.
      headers: {
        Authorization: getSessionObject("user").token,
      },
    };
    const response = await fetch(`/api/users/${pseudo}`, options);

    if (!response.ok) {
      throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
      );
    }
    const userSelected = await response.json();
    //console.log("membre selectionné", userSelected);
    membreEmpechement = userSelected.pseudo;
    return userSelected;
  } catch (error) {
    console.error("AllUsersAdminPage for declined users::error: ", error);
  }
}

async function annulerParticipation(id) {
  try {
    const options = {
      method: "POST", // *GET, POST, PUT, DELETE, etc.
      headers: {
        Authorization: getSessionObject("user").token,
      },
    };
    const response = await fetch(
      `/api/users/annulerParticipation/${id}`,
      options
    );

    if (!response.ok) {
      throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
      );
    }
    
    getAllUsers();
    Redirect("/allUsersAdmin");
  } catch (error) {
    console.error(
      "AllUsersAdminPage setEtatInscriptionConfirmee::error: ",
      error
    );
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

    Redirect("/allUsersAdmin");
  } catch (error) {
    console.error(
        "ManageUsersPage setEtatInscriptionConfirmee::error: ",
        error
    );
  }
}

async function getAllUsers() {
  try {
    const options = {
      method: "GET", // *GET, POST, PUT, DELETE, etc.
      headers: {
        Authorization: getSessionObject("user").token,
      },
    };
    const response = await fetch(`/api/users/allUsers`, options);
    if (!response.ok) {
      throw new Error(
        "fetch error : " + response.status + " : " + response.statusText
      );
    }
    const users = await response.json();
    //console.log("tous les users", users);

    users.forEach((user) => {
      if (user.pseudo == membreEmpechement) {
        //ne pas mettre le membre empeché dans la liste
      } else {
        //console.log(membreEmpechement, "empechement");
        let libelleNotif = `Le membre "${membreEmpechement}" est empêché de participer à la donnerie`;
        myNotificationLibrary.addNotification(libelleNotif, user.id, "empechement");
      }
    });
  } catch (error) {
    console.error("AllUsersAdminPage for all users::error: ", error);
  }
}



export default AllUsersAdminPage;
