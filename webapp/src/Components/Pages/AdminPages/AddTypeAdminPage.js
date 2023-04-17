import {getSessionObject} from "../../../utils/session";

let pageDiv2 = document.querySelector("#page2");

let addTypePage = `
<br>
<div id="titre">
  <br>
  <h2>Ajouter un type  </h2>
  <br>
</div>
<br>
<div id="tabAddType">
  <div id="listeDeType"></div>
  <div id="addType">
    <p>Introduire un nouveau type :</p>
    <input type="text" id="libelle" placeholder="Ajoutez un type" class="form-control">
    <input id="ajoutType" type="submit" value="Ajouter">
    <br>
  </div>
</div>
<div id="divSucces"></div>`;

/**
 * Render the AddTypeAdminPage
 */
const AddTypeAdminPage = () => {
  pageDiv2.innerHTML = addTypePage;
  getTypes();

  let ajoutType = document.getElementById("ajoutType");
  ajoutType.addEventListener("click", addType);
};

async function getTypes() { // recuperer la liste de tous les types
  let typesList = document.getElementById("listeDeType");
  let htmlList = `<label for="selectOptionEtat">Liste de tout les types :</label>
    <select id="selectOptionEtat" class="form-select" aria-label="Default select example" required="true">`;
  try {
    const options = {
      method: "GET", // *GET, POST, PUT, DELETE, etc.
      headers: {
        Authorization: getSessionObject("user").token,
      },
    };
    const response = await fetch(`/api/types/allTypes`, options);

    if (!response.ok) {
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }

    const types = await response.json();

    if (types && types.length > 0) {
      types.forEach((element) => {
        htmlList += `<option value="${element.id}">${element.libelle}</option>`;
      });
    }

    htmlList += `</select>`;
    typesList.innerHTML = htmlList;
  } catch (error) {
    console.error("HomePage for last objects::error: ", error);
  }
}

async function addType() {
  try {
    let libelleType = document.getElementById("libelle");
    const options = {
      method: "POST", // *GET, POST, PUT, DELETE, etc.
      body: JSON.stringify({
        libelle: libelleType.value,
      }), // body data type must match "Content-Type" header
      headers: {
        "Content-Type": "application/json",
        Authorization: getSessionObject("user").token,
      },
    };
    const response = await fetch(`/api/types/`, options);
    if (!response.ok) {
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }

    document.getElementById("addType").innerHTML = `
    <p>Introduire un nouveau type </p>
    <input type="text" id="libelle" placeholder="Ajoutez un type" class="form-control">
    <input id="ajoutType" type="submit" value="Ajouter">
    <br>`;
    document.getElementById("divSucces").innerHTML += `
    <br>
    <div class="alert alert-success" role="alert">
      Le type a bien été ajouté
    </div>`;
    getTypes();
  } catch (error) {
    console.error("postObjectPage::error: ", error);
  }
}

export default AddTypeAdminPage;
