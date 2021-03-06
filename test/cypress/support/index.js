// ***********************************************************
// This example support/index.js is processed and
// loaded automatically before your test files.
//
// This is a great place to put global configuration and
// behavior that modifies Cypress.
//
// You can change the location of this file or turn off
// automatically serving support files with the
// 'supportFile' configuration option.
//
// You can read more here:
// https://on.cypress.io/configuration
// ***********************************************************

// Import commands.js using ES2015 syntax:
import './commands'

// Alternatively you can use CommonJS syntax:
// require('./commands')

Cypress.on('window:before:load', win => {

  // Cypress does not support cy.route() with fetch, set it to null and expect the polyfill to use XMLHttpRequest
  win.fetch = null;

  // Stub window.open for pdf file download tests
  cy.stub(win, 'open').as('windowOpen');
});
