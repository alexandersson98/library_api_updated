import './style.css'
import { Nav } from './components/navbar.js';
import { createRouter } from './router.js'

const app = document.querySelector("#app");
app.innerHTML = ` 
<header id="site-header"></header>
  <main id="outlet"></main>
  `;

  document.querySelector("#site-header").innerHTML = Nav();

  createRouter("#outlet");