import {Redirect} from "../Router/Router";
import ObjectLibrary from "../../Domain/ObjectLibrary";
import NotificationLibrary from "../../Domain/NotificationLibrary";
import {getSessionObject} from "../../utils/session";
import vImage from "../../img/iconeV.png";
import xImage from "../../img/iconeX.png";

const myObjectsLibrary = new ObjectLibrary();
const myNotificationLibrary = new NotificationLibrary();
const pageDiv = document.querySelector("#page");
const pageDiv2 = document.querySelector("#page2");
let tableInterest;
let errorStatus;
let test;

/**
 * Render the ObjectInterestPage
 */
const ObjectInterestPage = async () => {
  pageDiv.innerHTML = "";
  pageDiv2.innerHTML = "";
  //console.log("ici c'est lobjet : ", getSessionObject("objectInteret"));
  pageDiv.innerHTML = await myObjectsLibrary.getObject(
      getSessionObject("objectInteret").id
  );

  test = await getchosenReceiver(getSessionObject("objectInteret").id);

  //console.log(getSessionObject("objectInteret"));
};

async function getObjectInterest(id) { // on recupere la liste des personnes qui ont liké l'objet
  try {
    const options = {
      method: "GET", // *GET, POST, PUT, DELETE, etc.
      headers: {
        Authorization: getSessionObject("user").token,
      },
    };
    const response = await fetch(`/api/interest/${id}`, options);

    if (!response.ok) {
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }
    const interest = await response.json();
    //console.log(interest);
    //console.log(test);

    if (interest.length == 0) {
      tableInterest = `<br>
      <div id="titre">
        <br>
        <h2> Membres interessés : </h2>
        <br>
      </div>
      <br>
      <h2> Il n'y a aucun membre interessé pour l'instant`;
    } else {
      //console.log("on rentre ici et la taille ", interest.length);
      tableInterest = `
      <div id="messageErreur" class="alert alert-danger">
      <a id="message">  </a> </div>
      <br>
        
      <div id="titre">
        <br>
        <h2> Membres interessés : </h2>
        <br>
      </div>
      <br>
      <div id="tableAdmin">
        <table  class="table">
          <thead>
            <tr id="infoTete">
              <th class="col">Pseudo</th>  
              <th class="col">Date de disponibilité</th>
              <th class="col">Souhaite être appelé</th>
              <th class="col">N° téléphone</th>
              <th class="col"></th>
            </tr>
          </thead>
          <tbody>`;

      interest.forEach((users) => {
        tableInterest += `
            <tr data-id="${users.id_membre}">
              <td class="col">${users.membre}</td>
              <td class="col">${users.date_disponibilite}</td>`;

        if (users.appel) {
          tableInterest += `
              <td class="col"><img id="vImage" src=${vImage} href="#"></td>`;
        } else {
          tableInterest += `
              <td class="col"><img id="xImage" src=${xImage} href="#"></td>`;
        }

        if (users.num_telephone != undefined) {
          tableInterest += `
              <td class="col">${users.num_telephone}</td>`;
        } else {
          tableInterest += `
              <td class="col"></td>`;
        }

        tableInterest += `
              <td class="col"><button id="btnReposter" class="btn btn-primary choisir" type="button">Choisir ce receveur</button></td>
            </tr>`;
      });

      tableInterest += `
          </tbody>
        </table>
      </div>`;
    }

    pageDiv2.innerHTML += tableInterest;

    document.getElementById("messageErreur").style.display = "none";

    const chosenBtn = document.querySelectorAll(".choisir");
    chosenBtn.forEach((btn) => {
      btn.addEventListener("click", () => {
        let row = btn.closest("tr");
        let idMembre = row.dataset.id;
        //console.log(idMembre);
        //console.log("on parle de ce membre ", getSessionObject("objectInteret").id);
        chooseYourReceiver(idMembre, getSessionObject("objectInteret"));
        let libelleNotif = `Vous avez été désigné comme receveur pour l'objet : ${getSessionObject(
            "objectInteret").titre}`
        myNotificationLibrary.addNotification(libelleNotif, idMembre,
            "interet choisis");
        Redirect("/objectInterest");
      });
    });
  } catch (error) {
    console.error("ObjectInterestPage ::error: ", error);
  }
}

async function chooseYourReceiver(idMembre, object) { // permet d'indiquer un receveur parmi les likes
  try {
    const options = {
      method: "POST", // *GET, POST, PUT, DELETE, etc.
      body: JSON.stringify({
        id_membre: idMembre,
        id_objet: object.id,
      }), // body data type must match "Content-Type" header
      headers: {
        "Content-Type": "application/json",
        Authorization: getSessionObject("user").token,
      },
    };
    const response = await fetch(`/api/chosenInterest`, options);

    if (!response.ok) {
      errorStatus = response.status;
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }

  } catch (error) {
    let message = document.getElementById("message");
    document.getElementById("messageErreur").style.display = "block";

    if (errorStatus == 409) { //conflict
      message.innerText = "Vous avez déja essayer de choisir cette personne";
    }
    console.error("ObjectInterestPage ::error: ", error);
  }
}

async function getchosenReceiver(id) {
  try {
    const options = {
      method: "GET", // *GET, POST, PUT, DELETE, etc.
      headers: {
        Authorization: getSessionObject("user").token,
      },
    };
    const response = await fetch(`/api/chosenInterest/${id}`, options);
    if (!response.ok) {
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }
    const chosen = await response.json();
    //console.log("receveur choisi", chosen);
    let bool = false;

    chosen.forEach((users) => {
      if (users.etat_transaction == "En attente") {
        bool = true;
      }
    });

    if (!bool) {
      getObjectInterest(getSessionObject("objectInteret").id);
    } else {
      tableInterest = `
    <br>
    <div id="titre">
      <br>
      <h2> Receveur choisi :</h2>
      <br>
    </div>
    <br>
    <div id="tableAdmin">
      <table  class="table">
        <thead>
          <tr id="infoTete">
            <th class="col">Pseudo</th>  
            <th class="col">Etat transaction</th>
            <th class="col"></th>
            <th class="col"></th>
          </tr>
        </thead>
        <tbody>`;

      tableInterest += `
          <tr data-pseudo="${chosen[0].membre}">
            <td class="col">${chosen[0].membre}</td>
            <td class="col">${chosen[0].etat_transaction}</td>
            <td class="col"><button id="btnReposterEstVenu" class="btn btn-success estVenu" type="button">Le receveur est venu</button></td>
            <td class="col"><button id="btnReposterPasVenu" class="btn btn-danger pasVenu" type="button">Le receveur n'est pas venu</button></td>
          </tr>`;

      tableInterest += `
        </tbody>
      </table>
    </div>`;

      pageDiv2.innerHTML += tableInterest;

      const venuBtn = document.querySelectorAll(".estVenu");
      venuBtn.forEach((btn) => {
        btn.addEventListener("click", () => {
          //console.log(getSessionObject("objectInteret").id);
          receiverCame(getSessionObject("objectInteret").id);
        });
      });

      const pasVenuBtn = document.querySelectorAll(".pasVenu");
      pasVenuBtn.forEach((btn) => {
        btn.addEventListener("click", () => {
          //console.log(getSessionObject("objectInteret").id);
          receiverDidntCame(getSessionObject("objectInteret").id);
        });
      });
    }
    return chosen;
  } catch (error) {
    console.error("ObjectInterestPage::error: ", error);
  }
}

async function receiverCame(idObjet) { // indiquer si le receveur est venu
  try {
    const options = {
      method: "POST", // *GET, POST, PUT, DELETE, etc.
      body: JSON.stringify({
        id_objet: idObjet,
      }), // body data type must match "Content-Type" header
      headers: {
        "Content-Type": "application/json",
        Authorization: getSessionObject("user").token,
      },
    };
    const response = await fetch(`/api/chosenInterest/venu`, options);

    if (!response.ok) {
      errorStatus = response.status;
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }

    pageDiv.innerHTML = "";
    Redirect("/myObjects");
  } catch (error) {
    console.error("ObjectInterestPage ::error: ", error);
  }
}

async function setEtatChosenInterestFalse(id) {
  try {
    const options = {
      method: "POST",
      body: JSON.stringify({
        id_objet: id,
      }),
      headers: {
        "Content-Type": "application/json",
        Authorization: getSessionObject("user").token,
      },
    };

    const response = await fetch(`/api/chosenInterest/pasVenu`, options);
    if (!response.ok) {
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }

    let chosenInterest = await response.json();

    return chosenInterest;
  } catch (error) {
    console.error("ObjectInterestPage ::error: ", error);
  }
}

async function receiverDidntCame(id) { // indiquer que le receveur n'est pas venu
  try {
    const options = {
      method: "GET", // *GET, POST, PUT, DELETE, etc.
      headers: {
        Authorization: getSessionObject("user").token,
      },
    };
    const response = await fetch(`/api/interest/${id}`, options);

    if (!response.ok) {
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }
    const interest = await response.json();
    //console.log(interest);

    tableInterest = `
      <div id="messageErreur" class="alert alert-danger">
        <a id="message"></a>
      </div>
      <br>
      <div id="titre">
        <br>
        <h2> Veuillez choisir une de ces 3 options </h2>
        <br>
      </div>
      <br>
      <div id="tableAdmin">
      <table  class="table">
        <thead>
          <tr id="infoTete">
            <th class="col">Choisir un nouveau receveur</th>  
            <th class="col">Reposter l'objet</th>
            <th class="col">Annuler l'objet</th>
          </tr>
        </thead>
        <tbody>`;

    tableInterest += `
          <tr>
            <td class="col">
      <div id="tableAdmin">
        <table  class="table">
          <thead>
            <tr id="infoTete">
              <th class="col">Pseudo</th>  
              <th class="col">Date de disponibilité</th>
              <th class="col">Souhaite être appelé</th>
              <th class="col">N° téléphone</th>
              <th class="col"></th>
            </tr>
          </thead>
          <tbody>`;

    interest.forEach((users) => {
      tableInterest += `
            <tr data-id="${users.id_membre}">
              <td class="col">${users.membre}</td>
              <td class="col">${users.date_disponibilite}</td>`;

      if (users.appel) {
        tableInterest += `
              <td class="col"><img id="vImage" src=${vImage} href="#"></td>`;
      } else {
        tableInterest += `
              <td class="col"><img id="xImage" src=${xImage} href="#"></td>`;
      }

      if (users.num_telephone != undefined) {
        tableInterest += `
              <td class="col">${users.num_telephone}</td>`;
      } else {
        tableInterest += `
              <td class="col"></td>`;
      }

      tableInterest += `
              <td class="col"><button id="btnReposter" class="btn btn-primary choisir" type="button">Choisir ce receveur</button></td>
            </tr>`;
    });

    tableInterest += `
          </tbody>
        </table>
      </div>
      </td>`;

    tableInterest += `
      <td class="col"><button id="btnReposter" class="btn btn-success reposter" type="button">Reposter</button></td>
      <td class="col"><button id="btnAnnuler" class="btn btn-danger annuler" type="button">Annuler</button></td>
    </tr>`;

    tableInterest += `
  </tbody>
</table>
</div>`;

    pageDiv2.innerHTML = tableInterest;

    document.getElementById("messageErreur").style.display = "none";

    const chosenBtn = document.querySelectorAll(".choisir");
    chosenBtn.forEach((btn) => {
      btn.addEventListener("click", () => {
        let row = btn.closest("tr");
        let idMembre = row.dataset.id;
        //console.log(idMembre);
        //console.log(getSessionObject("objectInteret").id);
        setEtatChosenInterestFalse(getSessionObject("objectInteret").id);
        chooseYourReceiver(idMembre, getSessionObject("objectInteret"));

        let libelleNotif = `Vous avez été désigné comme receveur pour l'objet : ${getSessionObject(
            "objectInteret").titre}`
        myNotificationLibrary.addNotification(libelleNotif, idMembre,
            "interet choisis");
        Redirect("/objectInterest");
      });
    });

    const annulerBtn = document.querySelectorAll(".annuler");
    annulerBtn.forEach((btn) => {
      btn.addEventListener("click", () => {
        //console.log(idMembre);
        setEtatChosenInterestFalse(getSessionObject("objectInteret").id);
        annulerObject(getSessionObject("objectInteret").id);
      });
    });

    const reposterBtn = document.querySelectorAll(".reposter");
    reposterBtn.forEach((btn) => {
      btn.addEventListener("click", async () => {
        //console.log(idMembre);
        let ch = await setEtatChosenInterestFalse(
            getSessionObject("objectInteret").id);
        let libelleNotif = `L'objet "${getSessionObject(
            "objectInteret").titre}" a été remis en ligne car vous n'êtes pas venu`;
        myNotificationLibrary.addNotification(libelleNotif, ch.id_membre,
            "pas venu");
        reposertObject(getSessionObject("objectInteret").id);
        Redirect("/myObjects");
      });
    });
  } catch (error) {
    console.error("ObjectInterestPage ::error: ", error);
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

    pageDiv.innerHTML = "";
    Redirect("/myObjects");

  } catch (error) {
    console.error(error);
  }
}

async function annulerObject(id_objet) {
  try {
    const options = {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: getSessionObject("user").token,
      },
    };
    //console.log(id_objet);

    const response = await fetch(`/api/objects/${id_objet}`, options);

    if (!response.ok) {
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }
    
    //console.log(data);

    pageDiv.innerHTML = "";
    Redirect("/myObjects");
  } catch (error) {
    console.error(error);
  }
}

export default ObjectInterestPage;
