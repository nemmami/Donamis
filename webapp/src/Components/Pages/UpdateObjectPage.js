import {getSessionObject, removeSessionObject} from "../../utils/session";
import ObjectLibrary from "../../Domain/ObjectLibrary";
import {Redirect} from "../Router/Router";

let updatePage;
let photo;
const myObjectsLibrary = new ObjectLibrary();

updatePage = `
<form id="myForm">
  <div class="col"></div>
  <div class="col text-center">
    <div id="titre">
      <br>
      <h2> Mettez à jour votre objet : </h2>
      <br>
    </div>
    <br>
    <div id="tabUpdateObject">
      <form class="box" id="myForm">
        <input type="text" class="form-control" id="titreObj" placeholder="Titre">
        <input type="text" class="form-control" id="description" placeholder="Ajouter une description">
        <input type="text" class="form-control" id="plage_horaire" placeholder="Plages horaire dans lesquelles vous êtes disponible">
        <input class="form-control" name="file" type= "file" />
        <input id="myUpdateBtn" type="button" value="Mettre a jour">
      </form>
    </div>
  </div>
  <div class="col"></div>
</form>`;

/**
 * Render the UpdateObjectPage
 */
const UpdateObjectPage = async () => {
  const pageDiv2 = document.querySelector("#page2");
  pageDiv2.innerHTML = "";
  pageDiv2.innerHTML = await myObjectsLibrary.getObject(
      getSessionObject("object").id
  );

  pageDiv2.innerHTML += updatePage;

  let form = document.getElementById("myUpdateBtn");
  form.addEventListener("click", sendFile);
};

async function sendFile(e) {
  e.preventDefault();
  e.stopPropagation();
  if (document.querySelector("input[name=file]").files[0] === undefined) {
    updateObject();
    Redirect("/myObjects");
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

      updateObject();
      Redirect("/myObjects");
    } catch (error) {
      console.error("UploadFiles::error: ", error);
    }
  }
}

async function updateObject() {
  let id = getSessionObject("object").id;

  let titre = document.getElementById("titreObj").value;
  let description = document.getElementById("description").value;
  let plageHoraire = document.getElementById("plage_horaire").value;
  let idPhoto = "";
  if (photo) {
    idPhoto = photo.name.toString();
  }
  try {
    const options = {
      method: "PUT",
      body: JSON.stringify({
        titre: titre,
        description: description,
        plage_horaire: plageHoraire,
        photo: idPhoto,
      }),
      headers: {
        "Content-Type": "application/json",
        Authorization: getSessionObject("user").token,
      },
    };

    const response = await fetch(`/api/objects/UpdateObject/${id}`, options);

    if (!response.ok) {
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }

    removeSessionObject("object");
    Redirect("/myObjects");
  } catch (error) {
    console.error(error);
  }
}

export default UpdateObjectPage;
