import {getSessionObject} from "../../utils/session";
import {Redirect} from "../Router/Router";

/**
 * Render the ProfilPage
 */
const ProfilPage = () => {
  getPage();

  page.querySelectorAll("button").forEach((button) => { // redirect
    button.addEventListener("click", (e) => {
      Redirect(e.target.dataset.uri);
    });
  });
};

function getPage() {
  let profilInfo;
  let profil = `<button id="monProfil" class="btn btn-primary" data-uri="/profil"> Mon profil </button>`;
  profil += ` <button id="mesObjets" class="btn btn-primary" data-uri="/myObjects"> Mes objets </button>`;
  profil += ` <button id="mesObjets" class="btn btn-primary" data-uri="/myAssignObjects"> Objects reçus </button><br><br>`;

  if (getSessionObject("user")) {
    profilInfo = `
    <br>
    <div id="profil">
      <div id="titre">
        <br>
        <h2> Mon profil </h2>
        <br>
      </div>
      <div id="dTab">
        <div class="col" id="tabProfilBox1">
          <div>
            <a id="colonneProfil">Pseudo : </a>
            <a id="reponseProfil">${getSessionObject("user").pseudo}</a>
          </div>
          <div>
            <a id="colonneProfil">Nom : </a>
            <a id="reponseProfil">${getSessionObject("user").nom}</a>
          </div>
          <div>
            <a id="colonneProfil">Prénom : </a>
            <a id="reponseProfil">${getSessionObject("user").prenom}</a>
          </div>
          <div>
            <a id="colonneProfil">Mot de passe : </a>
            <a id="reponseProfil">******</a>
          </div>
          <br>
        </div>
        <div class="col" id="tabProfilBox2">
          <div>
            <a id="colonneProfil">Rue : </a>
            <a id="reponseProfil">${getSessionObject("user").rue}</a>
          </div>
          <div>
            <a id="colonneProfil">Code postal : </a>
            <a id="reponseProfil">${getSessionObject("user").codePostal}</a>
          </div>
          <div>
            <a id="colonneProfil">Numéro : </a>
            <a id="reponseProfil">${getSessionObject("user").numero}</a>
          </div>`;

    if (getSessionObject("user").boite != null) {
      profilInfo += `
          <div>
            <a id="colonneProfil">Boîte : </a>
            <a id="reponseProfil">${getSessionObject("user").boite}</a>
          </div>`;
    }

    profilInfo += `
          <div>
            <a id="colonneProfil">Ville : </a>
            <a id="reponseProfil">${getSessionObject("user").ville}</a>
          </div>
        </div>
      </div>
    </div>`;
  }

  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = profil;

  const pageDiv2 = document.querySelector("#page2");
  pageDiv2.innerHTML = profilInfo;
}

export default ProfilPage;
