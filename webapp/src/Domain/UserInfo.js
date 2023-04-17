import { getSessionObject, setSessionObject } from "../utils/session";
class UserInfo {
  async getAllUserInformation(idUser) { // permet de recuperer le token du membre
    try {
      const options = {
        method: "POST", // *GET, POST, PUT, DELETE, etc.
        body: JSON.stringify({
          id: idUser,
        }),
        headers: {
          "Content-Type": "application/json",
          Authorization: getSessionObject("user").token,
        },
      };
      const response = await fetch(`/api/users/token`, options); // fetch return a promise => we wait for the response

      if (!response.ok) {
        throw new Error(
          "fetch error : " + response.status + " : " + response.statusText
        );
      }
      const newToken = await response.json(); // json() returns a promise => we wait for the data
      setSessionObject("user", newToken);
    } catch (error) {
      console.error("UserInfo::error: ", error);
    }
  }
}

export default UserInfo;
