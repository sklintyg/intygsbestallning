/// <reference types="Cypress" />

context('Login', () => {
  beforeEach(() => {
    cy.login('1');
  });

  it('verify current user shown', () => {
    cy.get('#currentUser').should('be.visible');
  });
});
