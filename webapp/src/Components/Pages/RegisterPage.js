import {Redirect} from "../Router/Router";

let registerPage;
let errorStatus;

registerPage = `
<div class="row" id="homePage">
  <div class="col"></div>
  <div class="col text-center">
    <br>
    <div id="titre">
      <br>
      <h2> Inscrivez-vous  </h2>
      <br>
    </div>
    <br>
    <form class="box">
      <div class="row" id="doubleTab">
        <div class="col box1" id="tabRegister">
          <br>
          <h4>Info personelle</h4>
          <input type="text" class="form-control" id="pseudo" placeholder="Pseudo" required>
          <input type="text" class="form-control" id="prenom" placeholder="Prénom" required>
          <input type="text" class="form-control" id="nom" placeholder="Nom" required>
          <input type="password" class="form-control" id="motDePasse" placeholder="Mot de passe" required>
          <input type="password" class="form-control" id="confirmPassword" placeholder="Confirmez votre mot de passe" required>
          <input type="checkbox" id="affichermdp"> Afficher votre mot de passe
        </div>
       
        <div class="col box2" id="tabRegister">
          <br>
          <h4>Adresse de résidence</h4>
          <input type="text" class="form-control" id="rue" placeholder="Rue" required>
          <input type="number" class="form-control" id="numero" placeholder="Numero" required>
          <input type="text" class="form-control" id="boite" placeholder="Boite">
          <input type="number" class="form-control" id="codePostal" placeholder="Code postal" required>
          <input type="text" class="form-control" id="ville" placeholder="Ville" required>
        </div>
      </div>
      <input id="btnInscrire" type="submit" value="S'inscire">
    </form>
    <br>
    <div id="messageErreur" class="alert alert-danger">
      <a id="message">  </a>
    </div>
    <div id="divSucces"> </div>
  </div>
  <div class="col"></div>
</div>`;

/**
 * Render the RegisterPage
 */
const RegisterPage = () => {
  const pageDiv2 = document.querySelector("#page2");
  pageDiv2.innerHTML = "";
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = registerPage;
  document.getElementById("messageErreur").style.display = "none";

  let form = document.querySelector("form");
  form.addEventListener("submit", onSubmit);

  let affichermdp = document.getElementById("affichermdp");
  affichermdp.addEventListener("change", (e) => { // pour afficher le motDePasse
    let mdp = document.getElementById("motDePasse");
    let mdpv = document.getElementById("confirmPassword");
    //console.log(e.target.checked)
    if (e.target.checked) {
      mdp.type = "text";
      mdpv.type = "text";
    } else {
      mdp.type = "password";
      mdpv.type = "password";
    }
  });

  async function onSubmit(e) {
    e.preventDefault();

    let pseudo = document.getElementById("pseudo");
    let nom = document.getElementById("nom");
    let prenom = document.getElementById("prenom");
    let motDePasse = document.getElementById("motDePasse");
    let confirmerMdp = document.getElementById("confirmPassword");
    let rue = document.getElementById("rue");
    let codePostal = document.getElementById("codePostal");
    let ville = document.getElementById("ville");
    let numero = document.getElementById("numero");
    let boite = document.getElementById("boite");

    try {
      if (motDePasse.value !== confirmerMdp.value) {
        let message = document.getElementById("message");
        document.getElementById("messageErreur").style.display = "block";
        message.innerText = "Mot de passe different";

        throw new Error("Mot de passe different");
      }
      const options = {
        method: "POST", // *GET, POST, PUT, DELETE, etc.
        body: JSON.stringify({
          pseudo: pseudo.value,
          nom: nom.value,
          prenom: prenom.value,
          motDePasse: motDePasse.value,
          rue: rue.value,
          codePostal: codePostal.value,
          ville: ville.value,
          numero: numero.value,
          boite: boite.value,
        }), // body data type must match "Content-Type" header
        headers: {
          "Content-Type": "application/json",
        },
      };

      const response = await fetch("/api/users/register", options); // fetch return a promise => we wait for the response

      if (!response.ok) {
        errorStatus = response.status;
        throw new Error(
            "fetch error : " + response.status + " : " + response.statusText
        );
      }
      const user = await response.json(); // json() returns a promise => we wait for the data
      //console.log("user registred", user);

      document.getElementById("messageErreur").style.display = "none";
      document.getElementById("divSucces").innerHTML = `
      <br>
      <div class="alert alert-success" role="alert">
        Vous vous êtes bien inscrit
      </div>`;
      setTimeout(function () {
        Redirect("/login");
      }, 1500);

    } catch (error) {
      let message = document.getElementById("message");
      document.getElementById("messageErreur").style.display = "block";

      if (errorStatus === 409) { //409 = Conflict
        message.innerText =
            "Veuillez choisir un autre pseudo, celui-ci est déjà utilisé";
      } else if (errorStatus === 400) { //400 = Bad request
        message.innerText = "Veuillez remplire tous les champs";
      } else if (errorStatus === 401) { //401 = Unauthorized
        message.innerText = "Vérifier vos champs";
      }

      console.error("RegisterPage::error: ", error);
    }
  }
};

export default RegisterPage;
