import userDb from "./userDb";

// This is a fake in-memory implementation of something
// that would be implemented by calling a REST server.

const fakeDatabase = {
  anvandare: userDb
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

    // Manipulate fakeDatabase anvandare state and return the new one..
    const newVardenhet = fakeDatabase.anvandare.vardgivare.reduce((arr, item) => arr.concat(item.vardenheter), []).find(
      ve => ve.id === hsaId);

    const newVardgivare = fakeDatabase.anvandare.vardgivare.find(vg => vg.id === newVardenhet.vardgivareHsaId);

    fakeDatabase.anvandare.valdVardenhet = newVardenhet;

    fakeDatabase.anvandare.valdVardgivare = newVardgivare;
    return fakeDatabase.anvandare;

  })
;
