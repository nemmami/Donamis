import Navbar from "../Navbar/Navbar";
import {Redirect} from "../Router/Router";
import {setSessionObject} from "../../utils/session";

let loginPage;
let errorStatus;
let rmCheck;

loginPage = `
<div class="row" id="homePage">
  <div class="col"></div>
  <div class="col text-center">
    <br>
    <div id="titre">
      <br>
      <h2> Connectez-vous : </h2>
      <br>
    </div>
    <br>
    <form id="formSend" class="box">
      <div id="tabLogin">
        <br>
        <input type="text" class="form-control" id="pseudo" placeholder="Pseudo" required>
        <input type="password" class="form-control" id="motDePasse" placeholder="Mot de passe" required>
        <input id ="btnConexion" type="submit" value="Se connecter"> <input type="checkbox" id="rememberMe"> Remember Me
        <br> 
        <br>
      </div>
    </form>
    <br>
    <div id="messageErreur" class="alert alert-danger">
      <a id="message">  </a>
    </div>
  </div>
  <div class="col"></div>
</div>`;

/**
 * Render the LoginPage
 */
const LoginPage = () => {
  const pageDiv2 = document.querySelector("#page2");
  pageDiv2.innerHTML = "";
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = loginPage;

  rmCheck = document.getElementById("rememberMe"); //recup info du rememberMe

  document.getElementById("messageErreur").style.display = "none";
  let form = document.querySelector("form");

  form.addEventListener("submit", onSubmit);
};

async function onSubmit(e) {

  e.preventDefault();
  let pseudo = document.getElementById("pseudo");
  const motDePasse = document.getElementById("motDePasse");
  //console.log("credentials", pseudo.value, motDePasse.value);
  try {
    const options = {
      method: "POST", // *GET, POST, PUT, DELETE, etc.
      body: JSON.stringify({
        pseudo: pseudo.value,
        motDePasse: motDePasse.value,
      }),
      headers: {
        "Content-Type": "application/json",
      },
    };

    const response = await fetch("/api/users/login", options);

    if (!response.ok) {
      errorStatus = response.status;
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }
    const user = await response.json();
    //console.log("user authenticated", user);
    // save the user into the localStorage or sessionStorage en fonction du btn remember me
    setSessionObject("user", user, rmCheck.checked);

    // Rerender the navbar for an authenticated user : temporary step prior to deal with token
    Navbar({isAuthenticated: true});

    // call the HomePage via the Router
    Redirect("/");
  } catch (error) {
    let message = document.getElementById("message");
    document.getElementById("messageErreur").style.display = "block";
    //console.log(errorStatus);

    //error message possible
    if (errorStatus === 401) {
      //401 = Unauthorized
      message.innerText =
          "Votre inscription est toujours en attente/est refusé";
    } else if (errorStatus === 404) {
      //404 = Not found
      message.innerText = "Pseudo ou mot de passe incorrect";
    } else if (errorStatus === 400) {
      //400 = Bad request
      message.innerText = "Veuillez remplire tous les champs";
    } else if (errorStatus === 403) {
      //403 = Forbidden
      getUserForbidden(pseudo.value);

    }

    console.error("LoginPage::error: ", error);
  }
}

async function getUserForbidden(pseudo) {
  try {
    const options = {
      method: "GET", // *GET, POST, PUT, DELETE, etc.
      headers: {
        "Content-Type": "application/json",
      },
    };
    const response = await fetch(
        `/api/users/getRaisonRefus/${pseudo}`,
        options
    ); // fetch return a promise => we wait for the response

    if (!response.ok) {
      throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
      );
    }
    const user = await response.json(); // json() returns a promise => we wait for the data
    //console.log("raison du refus", user.raison);
    let message = document.getElementById("message");
    if (user.raison != null) {
      message.innerText = `Votre inscription est refusée pour la raison suivante : 
      ${user.raison}`;
    } else {
      message.innerText = "Votre inscription est toujours en attente";
    }

  } catch (error) {
    console.error("LoginPage::error: ", error);
  }
}

export default LoginPage;
