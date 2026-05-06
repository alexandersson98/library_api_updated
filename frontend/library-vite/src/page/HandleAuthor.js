export function renderAuthorPage(container) {
  container.innerHTML = `
    <div class="page">
      <h1>Authors</h1>

      <section>
        <h2>Create Author — POST /api/v1/author</h2>
        <form id="form-create-author">
          <label>Name<input type="text" name="name" /></label>
          <button type="submit">Create</button>
        </form>
        <div id="result-create-author"></div>
      </section>

      <section>
        <h2>Get Author by ID — GET /api/v1/author/{id}</h2>
        <form id="form-get-author">
          <label>Author ID<input type="number" name="id" /></label>
          <button type="submit">Get</button>
        </form>
        <div id="result-get-author"></div>
      </section>

      <section>
        <h2>Get Books by Author — GET /api/v1/author/{authorId}/books</h2>
        <form id="form-get-author-books">
          <label>Author ID<input type="number" name="authorId" /></label>
          <label>Page<input type="number" name="page" value="0" /></label>
          <label>Size<input type="number" name="size" value="10" /></label>
          <button type="submit">Get Books</button>
        </form>
        <div id="result-get-author-books"></div>
      </section>
    </div>
  `
}
