// Import Bootstrap CSS
import "bootstrap/dist/css/bootstrap.min.css";
// Custom styles
import "./stylesheets/main.css";

// This is the entry point to your app : add all relevant import and custom code
import NavBar from "./Components/Navbar/Navbar";
import Footer from "./Components/Footer/Footer";
import { Router } from "./Components/Router/Router";
import { getSessionObject } from "./utils/session";
import UserInfo from "./Domain/UserInfo";

if (getSessionObject("user")) {
  const myUserInfo = new UserInfo();
  myUserInfo.getAllUserInformation(getSessionObject("user").id);
}

NavBar();

Footer();

Router();
