/// <reference types="Cypress" />

context('Skriv ut', () => {

  let bestallningId;

  beforeEach(() => {
    cy.login("Simona Samordnare (Samordnare 1 | Intygsbeställning)");
    cy.addBestallning().then(response => {
      expect(response.body.entity).to.have.property("id");
      bestallningId = response.body.entity.id;
    });
  });

  afterEach(() => {
    cy.removeBestallning(bestallningId);
  });

  it('verify forfragan pdf', () => {
    cy.get("#BestallningFilterBar-Aktiva").click();
    cy.get("#BestallningListButton-" + bestallningId).click();
    cy.get('#skrivUtBtn').click();
    cy.get('#skrivUtForfraganBtn').click();

    let pdfUrl = '/api/bestallningar/' + bestallningId + '/pdf/forfragan';
    cy.get('@windowOpen').should('be.calledWith', pdfUrl);

    cy.request(pdfUrl).its('headers').its('content-type').should('be', 'application/pdf');
  });

  it('verify fakturaunderlag pdf', () => {
    cy.get("#BestallningFilterBar-Aktiva").click();
    cy.get("#BestallningListButton-" + bestallningId).click();
    cy.get('#skrivUtBtn').click();
    cy.get('#skrivUtFakturaunderlagBtn').click();

    let pdfUrl = '/api/bestallningar/' + bestallningId + '/pdf/faktureringsunderlag';
    cy.get('@windowOpen').should('be.calledWith', pdfUrl);

    cy.request(pdfUrl).its('headers').its('content-type').should('be', 'application/pdf');
  });

});
