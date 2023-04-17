import {getSessionObject, setSessionObject} from "../../utils/session";
import ObjectLibrary from "../../Domain/ObjectLibrary";
import {Redirect} from "../Router/Router";

const pageDiv2 = document.querySelector("#page2");
const myObjectsLibrary = new ObjectLibrary();
let allObjects;

allObjects = `
<br><br><button id="allObjects" class="btn btn-success" type"button">
Voir la liste de tout les objects offerts
</button>
`;

/**
 * Render the HomePage
 */
const HomePage = async () => {
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = "";

  pageDiv2.innerHTML = await myObjectsLibrary.getLastObjects();
  pageDiv2.innerHTML += allObjects;

  if(getSessionObject("user")) {
    getEtatUser(); // si le membre à un empechement
    setTimeout(function () {
      document.getElementById('messageAlert').innerHTML = "";
    }, 8000);
  }

  const btnEnvoyer = document.querySelectorAll(".moreInformation");

  btnEnvoyer.forEach((btn) => {
    btn.addEventListener("click", (e) => {
      if (!getSessionObject("user")) {
        Redirect("/login");
      } else {
        const elementId = e.target.value;
        getObject(elementId);
      }
    });
  });

  document.getElementById("allObjects").addEventListener("click", () => {
    if (getSessionObject("user")) {
      Redirect("/allObjects");
    } else {
      Redirect("/login");
    }
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

function getEtatUser() { // si il y a un empechement, on envoie une alerte
    const user = getSessionObject("user")
    //console.log("etat du membre ", user.etat_inscription);
    if (user.etat_inscription == "Empeché") {
      showNotification({
        top: 80,
        right: 30,
        html: 'Vous êtes actuellement considéré comme empeché, veuillez nous contactez pour régler cela',
        className: "notificationMessage"
      });
    }
}

function showNotification({top = 0, right = 0, className, html}) {

  let notification = document.createElement('div');
  notification.className = "notificationMessage";
  if (className) {
    notification.classList.add(className);
  }

  notification.style.top = top + 'px';
  notification.style.right = right + 'px';

  notification.innerHTML = html;
  document.body.append(notification);

  setTimeout(() => notification.remove(), 15000);

}

export default HomePage;
