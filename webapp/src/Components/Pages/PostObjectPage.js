import {getSessionObject} from "../../utils/session";
import {Redirect} from "../Router/Router";

let photo;
let postPage;
let errorStatus;

postPage = `
  <br>
  <div id="titre">
    <br>
    <h2> Offrir un objet :</h2>
    <br>
  </div>
  <br>
  <div id="postObjectDiv">
    <form class="box" id="myForm">
      <br>
      <input type="text" class="form-control" id="titreObjet" placeholder="Titre" required>
      <textarea name="textareaPostObject" class="form-control" id="description" rows="2" placeholder="Description" required></textarea>
      <textarea name="textareaPostObject" class="form-control" id="plage_horaire" rows="2" placeholder="Plage horaire durant laquelle vous êtes disponible" required></textarea>
      <div id="listeDeType">
      </div>
      <input class="form-control" name="file" type= "file" />
      <input id="myBtnPoster" type="submit" value="Poster">
    </form>
  </div>
  <br>
  <div id="messageErreur" class="alert alert-danger">
    <a id="message">  </a>
  </div>
  <div id="divSucces"></div>`;

/**
 * Render the PostObjectPage
 */
const PostObjectPage = () => {
  const pageDiv2 = document.querySelector("#page2");
  pageDiv2.innerHTML = postPage;
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = "";
  document.getElementById("messageErreur").style.display = "none";

  getTypes();

  let form = document.getElementById("myBtnPoster");
  form.addEventListener("click", sendFile);
};

async function getTypes() { // récuperer la liste de tout les types
  let typesList = document.getElementById("listeDeType");
  let htmlList = `<label for="selectOptionEtat">Veuillez choisir un type pour votre objet :</label>
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
    console.error("PostObjectPage for last objects::error: ", error);
  }
}

async function sendFile(e) { // permet d'ajouter une photo
  e.preventDefault();
  e.stopPropagation();
  if (document.querySelector("input[name=file]").files[0] === undefined) {
    onSubmit();
  } else {
    try {
      const fileInput = document.querySelector("input[name=file]");

      const formData = new FormData();

      formData.append("file", fileInput.files[0]);
      const options = {
        method: "POST",
        body: formData,
        headers: {
          Authorization: getSessionObject("user").token,
        },
      };

      const response = await fetch("/api/objects/upload", options);
      if (!response.ok) {
        throw new Error(
            "fetch error : " + response.status + " : " + response.statusText
        );
      }

      photo = await response.json();

      onSubmit();
      Redirect("/");
    } catch (error) {
      console.error("UploadFiles::error: ", error);
    }
  }
}

async function onSubmit() {
  var select = document.getElementById("selectOptionEtat");
  let idType = parseInt(select.options[select.selectedIndex].value);
  let titre = document.getElementById("titreObjet");
  let description = document.getElementById("description");
  let plage_horaire = document.getElementById("plage_horaire");
  let idPhoto = "";
  if (photo) {
    idPhoto = photo.name.toString();
  }

  try {
    const options = {
      method: "POST", // *GET, POST, PUT, DELETE, etc.
      body: JSON.stringify({
        id_type: idType, // recup tout les id des types
        titre: titre.value,
        description: description.value,
        photo: idPhoto,
        plage_horaire: plage_horaire.value,
        membre_offreur: getSessionObject("user").id,
      }), // body data type must match "Content-Type" header
      headers: {
        "Content-Type": "application/json",
        Authorization: getSessionObject("user").token,
      },
    };

    const response = await fetch("/api/objects/insert", options); // fetch return a promise => we wait for the response

    if (!response.ok) {
      errorStatus = response.status;
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }
    document.getElementById("divSucces").innerHTML = `
    <br>
    <div class="alert alert-success" role="alert">
      L'objet a bien été ajouté
    </div>`;

    setTimeout(function () {
      Redirect("/");
    }, 1500);
  } catch (error) {
    let message = document.getElementById("message");
    document.getElementById("messageErreur").style.display = "block";

    //error message possible
    if (errorStatus === 400) { //400 = Bad Request
      message.innerText = "Veuillez remplire tous les champs";
    }
    console.error("PostObjectPage::error: ", error);
  }
}

export default PostObjectPage;
