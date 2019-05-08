/// <reference types="Cypress" />

context('Change unit', () => {

  let bestallningId;

  beforeEach(() => {
    cy.addBestallning().then(response => {
      expect(response.body.entity).to.have.property("id");
      bestallningId = response.body.entity.id;
    });
  });

  afterEach(() => {
    cy.removeBestallning(bestallningId);
  });

  it('verify user can change unit and see bestallning for that unit', () => {

    cy.server();
    cy.route('/api/bestallningar*').as('listBestallningar');

    cy.login('2');

    cy.get('#selectUnitBtn-centrum-ost').click();
    cy.get('#currentUserTitle').should('contain.text', 'Harald Alltsson');
    cy.get('#currentVardgivarTitle').should('contain.text', 'Landstinget Västmanland');
    cy.get('#currentVardenhetTitle').should('contain.text', 'Vårdcentrum i Öst');
    cy.get("#BestallningFilterBar-Aktiva").click();
    cy.wait('@listBestallningar');
    cy.get("#BestallningListButton-" + bestallningId).should('not.be.visible');

    cy.get('#changeUnitBtn').click();
    cy.get('#selectUnitBtn-IFV1239877878-1042').click();
    cy.get('#currentUserTitle').should('contain.text', 'Harald Alltsson');
    cy.get('#currentVardgivarTitle').should('contain.text', 'WebCert-Vårdgivare1');
    cy.get('#currentVardenhetTitle').should('contain.text', 'WebCert-Enhet1');
    cy.get("#BestallningFilterBar-Aktiva").click();
    cy.get("#BestallningListButton-" + bestallningId).should('be.visible');
  });
});
