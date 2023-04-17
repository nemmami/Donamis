import {getSessionObject} from "../utils/session";

class ObjectLibrary {
  async getLastObjects() { // permet de recuperer les 6 derniers objets
    try {
      const response = await fetch(`/api/objects/lastObjects`);
      if (!response.ok) {
        throw new Error(
            "fetch error : " + response.status + " : " + response.statusText
        );
      }
      const lastObjects = await response.json();
      //console.log("6 dernier objets", lastObjects);

      function importAll(r) {
        let images = {};
        r.keys().map((item, index) => {
          images[item.replace("./", "")] = r(item);
        });
        return images;
      }

      const images = importAll(
          require.context("../../../photo", false, /\.(png|jpe?g|svg)$/)
      );

      let tableLastObjects = `
          <br>
          <div id="titre">
            <br>
            <h2> Les derniers objets  </h2>
            <br>
          </div>
          <br>
          <div id="tableUsers">
            <table  class="table" >
              <thead id="couleurOslm">
                <tr id ="infoTete">
                  <th class="col">Titre</th>
                  <th class="col">Description</th>  
                  <th class="col">Photo</th>
                  <th class="col">Date</th>
                  <th class="col">Membre</th>
                  <th class="col"></th>
                </tr>
              </thead>
              <tbody>`;

      lastObjects.forEach((object) => {
        //this.getPathPhoto(object.id)
        if (object.photo === null) {
          object.photo = "default.jpg";
        }
        tableLastObjects += `
                <tr data-id="${object.id}">
                  <td class="col">${object.titre}</td>
                  <td class="col">${object.description}</td>
                  <td class="col"><img id="imageHome" src=${
            images[object.photo]
        } href="#"> </td> 
                  <td class="col">${object.date}</td>
                  <td class="col">${object.membre_offreur}</td>
                  <td class="col">
                    <button id="btnEnvoyer" class="btn btn-primary moreInformation" value="${
            object.id
        }" type"button">
                      Plus d'information
                    </button>
                  </td>
                </tr>`;
      });

      tableLastObjects += `
              </tbody>
            </table>
          </div>`;

      return tableLastObjects;
    } catch (error) {
      console.error("HomePage lastObjects::error", error);
    }
  }

  async getAllObjectsOffered() { // permet de recuperer tout les objets offert
    try {
      const options = {
        method: "GET", // *GET, POST, PUT, DELETE, etc.
        headers: {
          Authorization: getSessionObject("user").token,
        },
      };
      const response = await fetch(`/api/objects/allObjectsOffered`, options);
      if (!response.ok) {
        throw new Error(
            "fetch error : " + response.status + " : " + response.statusText
        );
      }
      const allObjectsOffered = await response.json();
      //console.log("Tout les objets", allObjectsOffered);

      function importAll(r) {
        let images = {};
        r.keys().map((item, index) => {
          images[item.replace("./", "")] = r(item);
        });
        return images;
      }

      const images = importAll(
          require.context("../../../photo", false, /\.(png|jpe?g)$/)
      );

      let tableAllObjectsOffered = `
          <br>
          <div id="titre">
            <br>
            <h2> Tout les objets  </h2>
            <br>
          </div>
          <br>
          <div id="tableUsers">
            <table class="table">
              <thead>
                <tr id="infoTete">
                  <th class="col">Titre</th>
                  <th class="col">Description</th>  
                  <th class="col">Photo</th>
                  <th class="col">Date</th>
                  <th class="col">Membre</th>
                  <th class="col"></th>
                </tr>
              </thead>
              <tbody>`;

      allObjectsOffered.forEach((object) => {
        if (object.photo === null) {
          object.photo = "default.jpg";
        }
        tableAllObjectsOffered += `
                <tr data-id="${object.id}">
                  <td class="col">${object.titre}</td>
                  <td class="col">${object.description}</td>
                  <td class="col"><img id="imageHome" src=${
            images[object.photo]
        } href="#"> </td> 
                  <td class="col">${object.date}</td>
                  <td class="col">${object.membre_offreur}</td>
                  <td class="col">
                    <button id="btnEnvoyer" class="btn btn-primary moreInformation" value="${
            object.id
        }" type"button">
                      Plus d'information
                    </button>
                  </td>
                </tr>`;
      });

      tableAllObjectsOffered += `
              </tbody>
            </table>
          </div>`;

      return tableAllObjectsOffered;
    } catch (error) {
      console.error("HomePage allObjectsOffered::error", error);
    }
  }

  async getAllObjects() { // permet d'avoir tout les objets (pour l'admin)
    try {
      const options = {
        method: "GET", // *GET, POST, PUT, DELETE, etc.
        headers: {
          Authorization: getSessionObject("user").token,
        },
      };
      const response = await fetch(`/api/objects/allObjects`, options);
      if (!response.ok) {
        throw new Error(
            "fetch error : " + response.status + " : " + response.statusText
        );
      }
      const allObjectsOffered = await response.json();
      //console.log("Tout les objets", allObjectsOffered);

      function importAll(r) {
        let images = {};
        r.keys().map((item, index) => {
          images[item.replace("./", "")] = r(item);
        });
        return images;
      }

      const images = importAll(
          require.context("../../../photo", false, /\.(png|jpe?g)$/)
      );

      let tableAllObjectsOffered = `
          <br>
          <div id="titre">
            <br>
            <h2>Tout les objets  </h2>
            <br>
          </div>
          <br>
          <div id="tableUsers">
            <table class="table">
              <thead>
                <tr id ="infoTete">
                  <th class="col">Titre</th>
                  <th class="col">Description</th> 
                  <th class="col">Photo</th>
                  <th class="col">Date</th>     
                  <th class="col">Membre</th>
                  <th class="col">Etat</th>
                  <th class="col"></th>
                </tr>
              </thead>
              <tbody>`;

      allObjectsOffered.forEach((object) => {
        //this.getPathPhoto(object.id)
        if (object.photo === null) {
          object.photo = "default.jpg";
        }
        tableAllObjectsOffered += `
                <tr data-id="${object.id}">
                  <td class="col">${object.titre}</td>
                  <td class="col">${object.description}</td>
                  <td class="col"><img id="imageHome" src=${
            images[object.photo]
        } href="#"> </td>
                   <td class="col">${object.date}</td>
                   <td class="col">${object.membre_offreur}</td>
                  <td class="col">${object.etat}</td>
                  <td class="col">
                    <button id="btnEnvoyer" class="btn btn-primary moreInformation" value="${
            object.id
        }" type"button">
                      Plus d'information
                    </button>
                  </td>
                </tr>`;
      });

      tableAllObjectsOffered += `
              </tbody>
            </table>
          </div>`;

      return tableAllObjectsOffered;
    } catch (error) {
      console.error("HomePage allObjectsOffered::error", error);
    }
  }

  async getObject(id) { // permet d'avoir toutes les infos d'un objet
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

      function importAll(r) {
        let images = {};
        r.keys().map((item, index) => {
          images[item.replace("./", "")] = r(item);
        });
        return images;
      }

      const images = importAll(
          require.context("../../../photo", false, /\.(png|jpe?g)$/)
      );

      if (object.photo === null) {
        object.photo = "default.jpg";
      }
      let tableWaiting = `
        <br>
        <div id="titre">
          <br>
          <h2>Object selectionné  </h2>
          <br>
        </div>
        <div>
          <br>
          <table id="tableObject" class="table">
            <thead>
              <tr id="infoTete">  
                <th class="col">Type</th>
                <th class="col">Titre</th>
                <th class="col">Description</th>
                <th class="col">Photo</th>
                <th class="col">Disponibilité</th>  
                <th class="col">Membre</th>
                <th class="col">Date d'ajout</th>
                <th class="col">Nombre de like</th>
              </tr>
            </thead>
            <tbody>
              <tr data-id="${object.id}">
                <td class="col">${object.id_type}</td>
                <td class="col">${object.titre}</td>
                <td class="col">${object.description}</td>
                <td class="col"><img id="imageHome" src=${
          images[object.photo]
      } href="#"> </td> 
                <td class="col">${object.plage_horaire}</td>
                <td class="col">${object.membre_offreur}</td>
                <td class="col">${object.date}</td>
                <td class="col">${object.nbr_interesse}</td>
              </tr>
            </tbody>
          </table>
        </div>`;

      return tableWaiting;
    } catch (error) {
      console.error("HomePage for last objects::error: ", error);
    }
  }

}

export default ObjectLibrary;
