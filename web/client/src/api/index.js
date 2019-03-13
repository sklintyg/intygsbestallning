import anvandare from "./anvandare";

// This is a fake in-memory implementation of something
// that would be implemented by calling a REST server.

const fakeDatabase = {
  anvandare: anvandare
};

const delay = (ms) =>
  new Promise(resolve => setTimeout(resolve, ms));

// should actually return  return makeServerRequest('anvandare')
export const fetchAnvandare = () =>
  delay(500).then(() => {
      return fakeDatabase.anvandare;

    });
// should actually return  return makeServerRequest('anvandare/andra-enhet')
export const changeEnhet = (hsaId) =>
  delay(500).then(() => {
    fakeDatabase.anvandare.valdVardenhet = {id:'99', namn:'Nya enheten'};
    return fakeDatabase.anvandare;

  });
