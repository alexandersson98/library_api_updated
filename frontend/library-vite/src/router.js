import { HandleBook, mountHandleBook } from "./page/HandleBook";
import { HandleAuthor, mountHandleAuthor } from "./page/HandleAuthor";
import { HandleLoan, mountHandleLoan } from "./page/HandleLoan";
import { Nav } from "./components/navbar";


export function createRouter(outletSelector) {
  const outlet = document.querySelector(outletSelector);
  if (!outlet) throw new Error(`Hittar inte element: ${outletSelector}`);

  const header = document.querySelector("#site-header");


  const routes = {
    "/": {
      view: HandleBook,
      mount: mountHandleBook,
      showNav: true,
    },

    "/authors": {
      view: HandleAuthor,
      mount: mountHandleAuthor,
      showNav: true,
    },

    "/loans": {
      view: HandleLoan,
      mount: mountHandleLoan,
      showNav: true,
    },

    notFound: {
      view: () => `<h1>404</h1><p>Sidan finns inte.</p>`,
      mount: null,
      showNav: false,
    },
  };

  function parseHash() {
    const raw = (location.hash || "#/").slice(1); 
    const [pathPart, queryPart] = raw.split("?");
    const parts = (pathPart || "/").split("/").filter(Boolean);

    const path = parts.length === 0 ? "/" : `/${parts[0]}`;
    const rest = parts.slice(1);
    const params = Object.fromEntries(new URLSearchParams(queryPart || ""));

    return { path, rest, params };
  }

  function ensureNavMounted() {
    if (!header) return;
    if (header.dataset.mounted === "1") return;
    header.innerHTML = Nav();
    header.dataset.mounted = "1";
  }

  function setNavVisible(visible) {
    if (!header) return;
    header.hidden = !visible;
    if (visible) ensureNavMounted();
  }

  async function render() {
  const { path, rest, params } = parseHash();
  const page = routes[path] || routes.notFound;

  setNavVisible(page.showNav !== false);

  const ctx = { path, rest, params };

  outlet.innerHTML = page.view(ctx);
  await page.mount?.(ctx);

  const hash = (location.hash || "#/").slice(1);
  document.querySelectorAll("#navLinks a, .browsebtns a").forEach(a => {
    const href = a.getAttribute("href")?.slice(1) || "/";
    a.setAttribute("aria-current", href === hash ? "page" : "false");
  });
}

  window.addEventListener("hashchange", render);
  render();
}