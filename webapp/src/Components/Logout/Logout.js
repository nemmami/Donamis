import Navbar from "../NavBar/Navbar";
import { Redirect } from "../Router/Router";
import { removeSessionObject } from "../../utils/session";

const Logout = () => {
  // clear the user session data from the localStorage or sessionStorage
  removeSessionObject("user");

  // re-render the navbar (for a non-authenticated user)
  Navbar();
  Redirect("/");
};

export default Logout;
