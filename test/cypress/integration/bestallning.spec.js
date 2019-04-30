/// <reference types="Cypress" />

context("Bestallning", () => {
  let bestallningId;

  beforeEach(() => {
    cy.login("0");
    cy.addBestallning().then(response => {
      expect(response.body.entity).to.have.property("id");
      bestallningId = response.body.entity.id;
    });
  });

  afterEach(() => {
    cy.removeBestallning(bestallningId);
  });

  it("Should return to correct list after accept", () => {
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

  it("Should have rejected bestallning in avvisad", () => {
    cy.get("#BestallningFilterBar-Aktiva").click();
    cy.get("#BestallningListTable").should("be.visible");
    cy.get("#BestallningListButton-" + bestallningId).click();

    cy.get("#BestallningAvvisaActionButton").click();
    cy.get("#AvvisaDialogRadioAvvisa").click();
    cy.get("#AvvisaDialogConfirmButton").should("be.visible");
    cy.get("#AvvisaDialogConfirmButton").click();

    cy.get("#BestallningBackToList").click();
    cy.get("#BestallningFilterBar-Avvisade").click();
    cy.get("#BestallningListButton-" + bestallningId).should("be.visible");
  })

  it("Should remove bestallning from list when deleting", () => {
    cy.get("#BestallningFilterBar-Aktiva").click();
    cy.get("#BestallningListTable").should("be.visible");
    cy.get("#BestallningListButton-" + bestallningId).click();

    cy.get("#BestallningAvvisaActionButton").click();
    cy.get("#AvvisaDialogRadioDelete").click();
    cy.get("#AvvisaDialogConfirmButton").should("be.visible");
    cy.get("#AvvisaDialogConfirmButton").click();

    cy.get("#BorttagenDialogConfirmButton").click();
    cy.get("#BestallningFilterBar-Avvisade").click();
    cy.get("#BestallningListButton-" + bestallningId).should("not.be.visible");
  })
});
