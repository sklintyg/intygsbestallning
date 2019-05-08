/// <reference types="Cypress" />

context('Login', () => {
  it('verify current user and cookiebanner', () => {
    cy.login('1', false);
    cy.get('#currentUserTitle').should('contain.text', 'Ingbritt Filt');
    cy.get('#currentVardgivarTitle').should('contain.text', 'WebCert-Vårdgivare1');
    cy.get('#currentVardenhetTitle').should('contain.text', 'WebCert-Enhet1');

    cy.get('#cookieBanner').should('be.visible');
    cy.get('#cookieModal').should('not.be.visible');

    cy.get('#cookiesReadMoreBtn').click();
    cy.get('#cookieModal').should('be.visible');
    cy.get('#cookieModalAbortBtn').click();
    cy.get('#cookieModal').should('not.be.visible');

    cy.get('#cookieBanner').should('be.visible');
    cy.get('#cookiesAcceptBtn').click();
    cy.get('#cookieBanner').should('not.be.visible');
  });
});
