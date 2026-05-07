export function Nav() {
  return `
    <nav id="navbar">
      <span class="nav-brand">Library API</span>
      <div class="nav-links" id="navLinks">
        <a href="#/authors" class="nav-link">Authors</a>
        <a href="#/" class="nav-link">Books</a>
        <a href="#/loans" class="nav-link">Loans</a>
      </div>
    </nav>
  `
}
