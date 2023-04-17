import {Redirect} from "../../Router/Router";

let admin;
let pageDiv = document.querySelector("#page2");
let pageDiv2 = document.querySelector("#page");

/**
 * Render the AdminPage
 */
const AdminPage = () => {
  pageDiv.innerHTML = ``;

  admin = `
  <button id="gererMembre" class="btn btn-primary" data-uri="/manageUsers"> Membre en attente/refusés </button>
  <button id="gererMembre" class="btn btn-primary" data-uri="/allUsersAdmin"> Membre confirmés </button>
  <button id="gererMembre" class="btn btn-primary" data-uri="/allObjectsAdmin"> Liste de tout les objets </button>
  <button id="gererMembre" class="btn btn-primary" data-uri="/addType"> Ajouter un type </button><br><br>`;

  pageDiv2.innerHTML = admin;

  pageDiv2.querySelectorAll("button").forEach((button) => { // redirect
    button.addEventListener("click", (e) => {
      Redirect(e.target.dataset.uri);
    });
  });
};

export default AdminPage;
