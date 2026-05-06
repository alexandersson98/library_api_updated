export function renderLoanPage(container) {
  container.innerHTML = `
    <div class="page">
      <h1>Loans</h1>

      <section>
        <h2>Create Loan — POST /api/v1/loans</h2>
        <form id="form-create-loan">
          <label>Book ID<input type="number" name="bookId" /></label>
          <button type="submit">Create</button>
        </form>
        <div id="result-create-loan"></div>
      </section>

      <section>
        <h2>Get Active Loans — GET /api/v1/loans</h2>
        <form id="form-get-active-loans">
          <label>Page<input type="number" name="page" value="0" /></label>
          <label>Size<input type="number" name="size" value="10" /></label>
          <button type="submit">Get Active</button>
        </form>
        <div id="result-get-active-loans"></div>
      </section>

      <section>
        <h2>Return Book — PATCH /api/v1/loans/{id}</h2>
        <form id="form-return-book">
          <label>Loan ID<input type="number" name="id" /></label>
          <button type="submit">Return</button>
        </form>
        <div id="result-return-book"></div>
      </section>

      <section>
        <h2>Loan History — GET /api/v1/loans/history</h2>
        <form id="form-loan-history">
          <label>Page<input type="number" name="page" value="0" /></label>
          <label>Size<input type="number" name="size" value="10" /></label>
          <button type="submit">Get History</button>
        </form>
        <div id="result-loan-history"></div>
      </section>
    </div>
  `
}
