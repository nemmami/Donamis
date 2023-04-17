import NotificationLibrary from "../../Domain/NotificationLibrary";
import {getSessionObject} from "../../utils/session";

const myNotificationLibrary = new NotificationLibrary();

let titre = `
<div id="titre">
    <br>
    <h2> Liste de toute vos notifications </h2>
</div>
<br>`;

/**
 * Render the NotificationPage
 */
const NotificationPage = async () => {
  const pageDiv2 = document.querySelector("#page2");
  pageDiv2.innerHTML = "";
  const pageDiv = document.querySelector("#page");
  pageDiv.innerHTML = titre;
  pageDiv.innerHTML += await myNotificationLibrary.getAllNotificationFromUser(
      getSessionObject("user").id
  );
};

export default NotificationPage;