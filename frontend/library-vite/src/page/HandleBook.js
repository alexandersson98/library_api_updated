import { api, showResult } from "../service/api/Api";

export function HandleBook() {
  return `
    <div class="page">
      <h1>Books</h1>

      <div class="card">
        <h2>Create Book — POST /api/v1/books</h2>
        <form id="form-create-book">
          <div class="form-group">
            <label>Title</label>
            <input type="text" name="title" />
          </div>
          <div class="form-group">
            <label>Author ID</label>
            <input type="number" name="authorId" />
          </div>
          <div class="form-group">
            <label>ISBN</label>
            <input type="text" name="isbn" />
          </div>
          <div class="form-group">
            <label>Published Year</label>
            <input type="number" name="publishedYear" />
          </div>
          <button type="submit" class="btn btn-primary">Create</button>
        </form>
        <div id="result-create-book"></div>
      </div>

      <div class="card">
        <h2>Get Book by ID — GET /api/v1/books/{id}</h2>
        <form id="form-get-book">
          <div class="form-group">
            <label>Book ID</label>
            <input type="number" name="id" />
          </div>
          <button type="submit" class="btn btn-primary">Get</button>
        </form>
        <div id="result-get-book"></div>
      </div>

      <div class="card">
        <h2>Get All Books — GET /api/v1/books</h2>
        <form id="form-get-all-books">
          <div class="form-group">
            <label>Page</label>
            <input type="number" name="page" value="0" />
          </div>
          <div class="form-group">
            <label>Size</label>
            <input type="number" name="size" value="10" />
          </div>
          <button type="submit" class="btn btn-primary">Get All</button>
        </form>
        <div id="result-get-all-books"></div>
      </div>

      <div class="card">
        <h2>Edit Book — PATCH /api/v1/books/edit/{id}</h2>
        <form id="form-edit-book">
          <div class="form-group">
            <label>Book ID</label>
            <input type="number" name="id" />
          </div>
          <div class="form-group">
            <label>Title</label>
            <input type="text" name="title" />
          </div>
          <div class="form-group">
            <label>Author ID</label>
            <input type="number" name="authorId" />
          </div>
          <div class="form-group">
            <label>ISBN</label>
            <input type="text" name="isbn" />
          </div>
          <div class="form-group">
            <label>Published Year</label>
            <input type="number" name="publishedYear" />
          </div>
          <button type="submit" class="btn btn-primary">Edit</button>
        </form>
        <div id="result-edit-book"></div>
      </div>

      <div class="card">
        <h2>Create Book V2 — POST /api/v2/books</h2>
        <form id="form-create-book-v2">
          <div class="form-group">
            <label>Title</label>
            <input type="text" name="title" />
          </div>
          <div class="form-group">
            <label>Author ID</label>
            <input type="number" name="authorId" />
          </div>
          <div class="form-group">
            <label>ISBN</label>
            <input type="text" name="isbn" />
          </div>
          <div class="form-group">
            <label>Published Year</label>
            <input type="number" name="publishedYear" />
          </div>
          <button type="submit" class="btn btn-primary">Create</button>
        </form>
        <div id="result-create-book-v2"></div>
      </div>

      <div class="card">
        <h2>Get Book by ID V2 — GET /api/v2/books/{id}</h2>
        <form id="form-get-book-v2">
          <div class="form-group">
            <label>Book ID</label>
            <input type="number" name="id" />
          </div>
          <button type="submit" class="btn btn-primary">Get</button>
        </form>
        <div id="result-get-book-v2"></div>
      </div>

      <div class="card">
        <h2>Get All Books V2 — GET /api/v2/books</h2>
        <form id="form-get-all-books-v2">
          <div class="form-group">
            <label>Page</label>
            <input type="number" name="page" value="0" />
          </div>
          <div class="form-group">
            <label>Size</label>
            <input type="number" name="size" value="10" />
          </div>
          <button type="submit" class="btn btn-primary">Get All</button>
        </form>
        <div id="result-get-all-books-v2"></div>
      </div>
    </div>
  `;
}

export async function mountHandleBook() {
  document.getElementById('form-create-book')?.addEventListener('submit', async e => {
    e.preventDefault()
    const d = Object.fromEntries(new FormData(e.target))
    const { ok, data } = await api('POST', '/api/v1/books', {
      title: d.title,
      authorId: Number(d.authorId),
      isbn: d.isbn,
      publishedYear: Number(d.publishedYear),
    })
    showResult('result-create-book', ok, data)
  })

  document.getElementById('form-get-book')?.addEventListener('submit', async e => {
    e.preventDefault()
    const { id } = Object.fromEntries(new FormData(e.target))
    const { ok, data } = await api('GET', `/api/v1/books/${id}`)
    showResult('result-get-book', ok, data)
  })

  document.getElementById('form-get-all-books')?.addEventListener('submit', async e => {
    e.preventDefault()
    const { page, size } = Object.fromEntries(new FormData(e.target))
    const { ok, data } = await api('GET', `/api/v1/books?page=${page}&size=${size}`)
    showResult('result-get-all-books', ok, data)
  })

  document.getElementById('form-edit-book')?.addEventListener('submit', async e => {
    e.preventDefault()
    const d = Object.fromEntries(new FormData(e.target))
    const body = {}
    if (d.title) body.title = d.title
    if (d.authorId) body.authorId = Number(d.authorId)
    if (d.isbn) body.isbn = d.isbn
    if (d.publishedYear) body.publishedYear = Number(d.publishedYear)
    const { ok, data } = await api('PATCH', `/api/v1/books/edit/${d.id}`, body)
    showResult('result-edit-book', ok, data)
  })

  document.getElementById('form-create-book-v2')?.addEventListener('submit', async e => {
    e.preventDefault()
    const d = Object.fromEntries(new FormData(e.target))
    const { ok, data } = await api('POST', '/api/v2/books', {
      title: d.title,
      authorId: Number(d.authorId),
      isbn: d.isbn,
      publishedYear: Number(d.publishedYear),
    })
    showResult('result-create-book-v2', ok, data)
  })

  document.getElementById('form-get-book-v2')?.addEventListener('submit', async e => {
    e.preventDefault()
    const { id } = Object.fromEntries(new FormData(e.target))
    const { ok, data } = await api('GET', `/api/v2/books/${id}`)
    showResult('result-get-book-v2', ok, data)
  })

  document.getElementById('form-get-all-books-v2')?.addEventListener('submit', async e => {
    e.preventDefault()
    const { page, size } = Object.fromEntries(new FormData(e.target))
    const { ok, data } = await api('GET', `/api/v2/books?page=${page}&size=${size}`)
    showResult('result-get-all-books-v2', ok, data)
  })
}
