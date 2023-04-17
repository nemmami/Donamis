import { getSessionObject } from "../utils/session";
import NavBar from "../Components/Navbar/Navbar";
class NotificationLibrary {

  async comapreDate(a, b) { // permet de comparer les dates (pour le tri)
    let date1 = new Date(a.dateNotif);
    let date2 = new Date(b.dateNotif);

    if (date1 < date2) {
      return -1;
    } else if (date1 > date2) {
      return 1;
    } else {
      return 0;
    }
  }

  async addNotification(libelle, id, type) { // on ajoute une notification
    try {
      const options = {
        method: "POST", // *GET, POST, PUT, DELETE, etc.
        body: JSON.stringify({
          id_membre: id,
          libelle: libelle,
          type: type,
        }), // body data type must match "Content-Type" header
        headers: {
          "Content-Type": "application/json",
          Authorization: getSessionObject("user").token,
        },
      };

      const response = await fetch(`api/notification/`, options);

      if (!response.ok) {
        throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
        );
      }
    } catch (error) {
      console.error("Notification::error", error);
    }
  }

  async areNewNotification(idMember) { // permet de savoir si il y a de nouvelles notification
    try {
      const options = {
        method: "GET", // *GET, POST, PUT, DELETE, etc.
        headers: {
          Authorization: getSessionObject("user").token,
        },
      };

      const response = await fetch(
        `api/notification/newNotif/${idMember}`,
        options
      );

      if (!response.ok) {
        throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
        );
      }

      const areNew = await response.json();
      //console.log("ici on regarde si il ya de nvl notif ", areNew);

      return areNew;
    } catch (error) {
      console.error("Notification::error", error);
    }
  }

  async setNotificationVueTrue(idNotif) { // permet de siganlé qu'on a vu la notification
    try {
      const options = {
        method: "POST", // *GET, POST, PUT, DELETE, etc.
        headers: {
          "Content-Type": "application/json",
          Authorization: getSessionObject("user").token,
        },
      };

      const response = await fetch(
        `api/notification/setNotifVueTrue/${idNotif}`,
        options
      );

      if (!response.ok) {
        throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
        );
      }
      //console.log("on est rentrer laaaaaaaaaa")
    } catch (error) {
      console.error("Notification::error", error);
    }
  }

  async getAllNotificationFromUser(idMember) { // permet de recuperer toutes les notifications
    try {
      const options = {
        method: "GET", // *GET, POST, PUT, DELETE, etc.
        headers: {
          Authorization: getSessionObject("user").token,
        },
      };

      const response = await fetch(
        `api/notification/allNotificationFromUser/${idMember}`,
        options
      );

      if (!response.ok) {
        throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
        );
      }
      const tabNotif = await response.json();
      //console.log(tabNotif);

      let htmlTab = `
        <div id="tabNotif">
        <section class="section-50">
              <div class="container">
                <div class="notification-ui_dd-content">`;

      if (tabNotif && tabNotif.length > 0) {
        tabNotif.forEach((element) => {
          if (element) {
            //console.log(element)
            if (element.notifVue == false) {
              //console.log("on rentre ici");
              this.setNotificationVueTrue(element.id_notif);
              NavBar();
            }
          }
          if (element.type == "interet") {
            htmlTab += `
              <div class="notification-list notification-list--unread">
                    <div class="notification-list_content">
                      <div class="notification-list_detail">
                        <p>${element.libelle}</p>
                      </div>
                    </div>
                    <div class="notification-list_feature-img"> <p>${element.date}</p> </div>
                  </div>`;
          } else if (element.type == "interet choisis") {
            htmlTab += `
              <div class="notification-list notification-list--unread-green">
              <div class="notification-list_content">
                <div class="notification-list_detail">
                  <p>${element.libelle}</p>
                </div>
              </div>
              <div class="notification-list_feature-img"> <p>${element.date}</p> </div>
            </div>`;
          } else if(element.type == "pas venu") {
            htmlTab += `
              <div class="notification-list notification-list--unread-red">
              <div class="notification-list_content">
                <div class="notification-list_detail">
                  <p>${element.libelle}</p>
                </div>
              </div>
              <div class="notification-list_feature-img"> <p>${element.date}</p> </div>
            </div>`;
          }else if(element.type == "empechement") {
            htmlTab += `
              <div class="notification-list notification-list--unread-red">
              <div class="notification-list_content">
                <div class="notification-list_detail">
                  <p>${element.libelle}</p>
                </div>
              </div>
              <div class="notification-list_feature-img"> <p>${element.date}</p> </div>
            </div>`;
          }
        });

        htmlTab += `
        </div>
        </div>
      </section>
        </div>`;
      } else {
        htmlTab += `
                <h2>Vos notifications sont vides</h2>`;
      }

      return htmlTab;
    } catch (error) {
      console.error("Notification::error", error);
    }
  }
}

export default NotificationLibrary;
