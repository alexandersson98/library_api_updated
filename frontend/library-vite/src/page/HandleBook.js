export function renderBookPage(container) {
  container.innerHTML = `
    <div class="page">
      <h1>Books</h1>

      <section>
        <h2>Create Book — POST /api/v1/books</h2>
        <form id="form-create-book">
          <label>Title<input type="text" name="title" /></label>
          <label>Author ID<input type="number" name="authorId" /></label>
          <label>ISBN<input type="text" name="isbn" /></label>
          <label>Published Year<input type="number" name="publishedYear" /></label>
          <button type="submit">Create</button>
        </form>
        <div id="result-create-book"></div>
      </section>

      <section>
        <h2>Get Book by ID — GET /api/v1/books/{id}</h2>
        <form id="form-get-book">
          <label>Book ID<input type="number" name="id" /></label>
          <button type="submit">Get</button>
        </form>
        <div id="result-get-book"></div>
      </section>

      <section>
        <h2>Get All Books — GET /api/v1/books</h2>
        <form id="form-get-all-books">
          <label>Page<input type="number" name="page" value="0" /></label>
          <label>Size<input type="number" name="size" value="10" /></label>
          <button type="submit">Get All</button>
        </form>
        <div id="result-get-all-books"></div>
      </section>

      <section>
        <h2>Edit Book — PATCH /api/v1/books/edit/{id}</h2>
        <form id="form-edit-book">
          <label>Book ID<input type="number" name="id" /></label>
          <label>Title<input type="text" name="title" /></label>
          <label>Author ID<input type="number" name="authorId" /></label>
          <label>ISBN<input type="text" name="isbn" /></label>
          <label>Published Year<input type="number" name="publishedYear" /></label>
          <button type="submit">Edit</button>
        </form>
        <div id="result-edit-book"></div>
      </section>

      <section>
        <h2>Create Book V2 — POST /api/v2/books</h2>
        <form id="form-create-book-v2">
          <label>Title<input type="text" name="title" /></label>
          <label>Author ID<input type="number" name="authorId" /></label>
          <label>ISBN<input type="text" name="isbn" /></label>
          <label>Published Year<input type="number" name="publishedYear" /></label>
          <button type="submit">Create</button>
        </form>
        <div id="result-create-book-v2"></div>
      </section>

      <section>
        <h2>Get Book by ID V2 — GET /api/v2/books/{id}</h2>
        <form id="form-get-book-v2">
          <label>Book ID<input type="number" name="id" /></label>
          <button type="submit">Get</button>
        </form>
        <div id="result-get-book-v2"></div>
      </section>

      <section>
        <h2>Get All Books V2 — GET /api/v2/books</h2>
        <form id="form-get-all-books-v2">
          <label>Page<input type="number" name="page" value="0" /></label>
          <label>Size<input type="number" name="size" value="10" /></label>
          <button type="submit">Get All</button>
        </form>
        <div id="result-get-all-books-v2"></div>
      </section>
    </div>
  `
}
