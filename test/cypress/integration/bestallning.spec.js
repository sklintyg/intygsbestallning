/// <reference types="Cypress" />

context("Login", () => {
  let bestallningId;
  beforeEach(() => {
    cy.login("0");
    cy.addBestallning().then(response => {
      expect(response.body.entity).to.have.property("id");
      bestallningId = response.body.entity.id;
    });
  });

  afterEach(() => {
    cy.request("DELETE", "/api/test/bestallningar/" + bestallningId);
  });

  it("Accept first bestallning", () => {
    cy.get("#BestallningFilterBar-Aktiva").click();
    cy.get("#BestallningListTable").should("be.visible");
    cy.get("#BestallningListButton-" + bestallningId).click();
    cy.get("#BestallningAcceptActionButton").click();
    cy.get("#AcceptDialogConfirmButton").should("be.visible");
    cy.get("#AcceptDialogConfirmButton").click();
    cy.get("#BestallningKlarActionButton").click();
    cy.get("#BestallningBackToList").click();
    cy.get("#BestallningFilterBar-Klarmarkerade").click();
    cy.get("#BestallningListTable").should("be.visible");
    cy.get("#BestallningListButton-" + bestallningId).click();
    cy.get("#BestallningBackToList").click();
    cy.url().should("include", "/bestallningar/KLARA");
  });
});
