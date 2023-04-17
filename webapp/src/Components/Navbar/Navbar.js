import { getSessionObject } from "../../utils/session";
import NotificationLibrary from "../../Domain/NotificationLibrary";
import homeNavBar from "../../img/homeNavbar.png";
import iconeProfil from "../../img/iconeProfil.png";
import iconeDeconnecter from "../../img/Logout.png";
import iconeNotification from "../../img/Notification.png";
import iconeEdit from "../../img/edit.png";

const myNotificationLibrary = new NotificationLibrary();

const Navbar = async () => {
  const navbarWrapper = document.querySelector("#navbarWrapper");
  let navbar;
  let user = getSessionObject("user");

  if (!user) {
    navbar = `
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
    <img id="homeLogo" src="${homeNavBar}" alt="homeLogo" href="#" data-uri="/">
    <button
      class="navbar-toggler"
      type="button"
      data-bs-toggle="collapse"
      data-bs-target="#navbarSupportedContent"
      aria-controls="navbarSupportedContent"
      aria-expanded="false"
      aria-label="Toggle navigation"
    >
    <span class="navbar-toggler-icon"></span>
    </button>
  
    
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">          
      </ul>
    </div>
  
    <div class="collapse navbar-collapse justify-content-end" id="navbarSupportedContent">
      <ul class="navbar-nav">
      <li class="nav-item">
      <a class="nav-link" href="#" data-uri="/login">Se connecter</a>
      </li>
      <li class="nav-item">
      <a class="nav-link" href="#" data-uri="/register">S'inscrire</a>
      </li>			
      </ul>		  
    </div>
  </div>
  </nav>
    `;
  } else {
    let areNewNotif = await myNotificationLibrary.areNewNotification(
      getSessionObject("user").id
    ); // permet d'afficher l'animation si il y a de nouvelles notifs
    
    navbar = `
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
    <img id="homeLogo" src="${homeNavBar}" alt="homeLogo" href="#" data-uri="/">
    <button
      class="navbar-toggler"
      type="button"
      data-bs-toggle="collapse"
      data-bs-target="#navbarSupportedContent"
      aria-controls="navbarSupportedContent"
      aria-expanded="false"
      aria-label="Toggle navigation"
    >
    <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item">
          <a class="nav-link" href="#" data-uri="/" id="navText"></a>
        </li>           
      </ul>
    </div>

    

    <div class="collapse navbar-collapse justify-content-end" id="navbarSupportedContent">
		  <ul class="navbar-nav">
	    
      <li class="nav-item">
        <a id="postez" class="nav-link" href="#" data-uri="/postObject">OFFRIR UN OBJET</a>
      </li>  
      
      <li class="nav-item">
        <a class="nav-item nav-link disabled" href="#"></a>
      </li>`;

    if (areNewNotif) {
      navbar += `
      <div class="notification">
      <li class="nav-item">
        <img id="iconeNotification" src="${iconeNotification}" alt="iconeNotification" href="#" data-uri="/notifPage">
      </li>`;
      if (user.admin) {
        navbar += `<div class="butten"></div>
      </div>`;
      } else {
        navbar += `<div class="buttenNonAdmin"></div>
      </div>`;
      }
    } else {
      navbar += `
      <li class="nav-item">
        <img id="iconeNotification" src="${iconeNotification}" alt="iconeNotification" href="#" data-uri="/notifPage">
      </li>`;
    }

    navbar += `
      <li class="nav-item">
        <img id="iconeProfil" src="${iconeProfil}" alt="iconeProfil" href="#" data-uri="/profil">
      </li>`;

    if (user.admin) {
      navbar += `
      <li class="nav-item">
        <img id="iconeEdit" src="${iconeEdit}" alt="iconeEdit" href="#" data-uri="/admin">
      </li>`;
    }

    navbar += `
      <li class="nav-item">
          <img id="iconeDeconnecter" src="${iconeDeconnecter}" alt="iconeProfil" href="#" data-uri="/logout">
      </li>      
		  </ul>  
		</div>
  </div>
  </nav>
`;
  }

  navbarWrapper.innerHTML = navbar;
};

export default Navbar;
