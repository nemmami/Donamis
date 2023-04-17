import {Redirect} from "../Router/Router";
import ObjectLibrary from "../../Domain/ObjectLibrary";
import NotificationLibrary from "../../Domain/NotificationLibrary";
import {getSessionObject, removeSessionObject} from "../../utils/session";

const myObjectsLibrary = new ObjectLibrary();
const myNotificationLibrary = new NotificationLibrary();
let errorStatus;

let marqueInteret = `
<div class="col text-center">
  <div id="messageErreurInterest" class="alert alert-danger">
    <a id="message">  </a>
  </div>
  <div id="divSucces"> </div>
  <br>
  <div id="interestForm">
    <form id="myForm">
      <div>
        <h3>Êtes vous interressé par l'object ? </h3>
      </div>
      <p>Veuillez introduire une date respecant les plages horaires proposés : </p>
      <textarea name="textareaPostObject" class="form-control" placeholder="Ajoutez vos disponibiltés ici" id="dateDisponible" type="text" rows="2"></textarea>
      <p>
        Souhaitez vous être appelé pour discuter des modalités pratiques du don ?
        <input type="checkbox" id="appel">
      </p>
      <div id="divTel"></div>
      <input id="myBtnInterest" type="submit" value="Like">
    </form>
    <br>
  </div>
</div>
<br>`;

/**
 * Render the ObjectPage
 */
const ObjectPage = async () => {
  const pageDiv2 = document.querySelector("#page2");
  pageDiv2.innerHTML = await myObjectsLibrary.getObject(
      getSessionObject("object").id
  );

  pageDiv2.innerHTML += marqueInteret;
  document.getElementById("messageErreurInterest").style.display = "none";

  let divAppel = document.getElementById("appel");
  divAppel.addEventListener("change", () => {
    if (divAppel.checked) {
      document.getElementById(
          "divTel"
      ).innerHTML += `<label for="phone">Entrez votre numéro de téléphone :</label>
      <input type="tel" id="phone" name="phone" class="form-control" required>
      <small>Format: 0411223344</small>`;
    } else {
      document.getElementById("divTel").innerHTML = "";
    }
  });

  let form = document.querySelector("form");
  form.addEventListener("submit", insertInterest);
};

async function insertInterest(e) { // permet de liker  l'objet
  e.preventDefault();
  let date = document.getElementById("dateDisponible").value;
  let appel = document.getElementById("appel");
  let numero = "";
  if (appel.checked) {
    numero = document.getElementById("phone").value;
  }

  if (
      getSessionObject("user").pseudo == getSessionObject(
          "object").membre_offreur
  ) {
    let message = document.getElementById("message");
    document.getElementById("messageErreurInterest").style.display = "block";
    message.innerText = "Vous ne pouvez pas liker votre propre object";
    throw new Error("Vous ne pouvez pas liker vôtre propre object");
  }

  try {
    const options = {
      method: "POST", // *GET, POST, PUT, DELETE, etc.
      body: JSON.stringify({
        id_membre: getSessionObject("user").id,
        id_objet: getSessionObject("object").id,
        date_disponibilite: date,
        appel: appel.checked,
        numero: numero,
      }), // body data type must match "Content-Type" header
      headers: {
        "Content-Type": "application/json",
        Authorization: getSessionObject("user").token,
      },
    };

    if (appel.checked) { // verification du format de numéro de téléphone
      if (
          numero.length != 10 ||
          (numero.charAt(0) != 0 && numero.charAt(1) != 4)
      ) {
        let message = document.getElementById("message");
        document.getElementById("messageErreurInterest").style.display =
            "block";
        message.innerText = "Mauvais format de numéro de téléphone";
        throw new Error("Mauvais format de numéro de téléphone");
      }

      for (let i = 0; i < numero.length; i++) {
        if (!isNumber(numero.charAt(i))) {
          let message = document.getElementById("message");
          document.getElementById("messageErreurInterest").style.display =
              "block";
          message.innerText = "Mauvais format de numéro de téléphone";
          throw new Error("Mauvais format de numéro de téléphone");
        }
      }
    }

    const response = await fetch("/api/interest", options); // fetch return a promise => we wait for the response

    if (!response.ok) {
      errorStatus = response.status;
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }

    let libelleNotif = `Le membre ${getSessionObject(
        "user").pseudo} a liker votre objet : ${getSessionObject(
        "object").titre} `;
    myNotificationLibrary.addNotification(libelleNotif,
        getSessionObject("object").id_membre_offreur, "interet");

    removeSessionObject("object");
    document.getElementById("messageErreurInterest").style.display = "none";
    document.getElementById("divSucces").innerHTML = `
    <br>
    <div class="alert alert-success" role="alert">
      Le like a bien été ajouté
    </div>`;
    setTimeout(function () {
      Redirect("/");
    }, 1500);
  } catch (error) {
    let message = document.getElementById("message");
    document.getElementById("messageErreurInterest").style.display = "block";

    if (errorStatus === 400) { //400 = Bad request
      message.innerText = "Veuillez remplir tout les champs";
    } else if (errorStatus === 409) { //409 = Conflict
      message.innerText = "Vous avez déjà liker cet objet";
    }

    console.error("ObjectPage::error: ", error);
  }
}

function isNumber(number) {
  if (number == 0) {
    return true;
  }

  if (number == 1) {
    return true;
  }

  if (number == 2) {
    return true;
  }

  if (number == 3) {
    return true;
  }

  if (number == 4) {
    return true;
  }

  if (number == 5) {
    return true;
  }

  if (number == 6) {
    return true;
  }

  if (number == 7) {
    return true;
  }

  if (number == 8) {
    return true;
  }

  if (number == 9) {
    return true;
  }

  return false;
}

export default ObjectPage;
