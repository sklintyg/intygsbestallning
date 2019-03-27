import userDb from "./userDb";
import { delay } from "./util";

// This is a fake in-memory implementation of something
// that would be implemented by calling a REST server.

const fakeDatabase = {
  anvandare: userDb
};

// should actually return  return makeServerRequest('anvandare')
export const fetchAnvandare = () =>
  delay(500).then(() => {
    return fakeDatabase.anvandare;

  });
export const logoutUser = () =>
  delay(1500).then(() => {
    return {};

  });
// should actually return  return makeServerRequest('anvandare/andra-enhet')
export const changeEnhet = (hsaId) =>
  delay(500).then(() => {

    // Manipulate fakeDatabase anvandare state and return the new one..
    const newVardenhet = fakeDatabase.anvandare.authoritiesTree.reduce((arr, item) => arr.concat(item.vardenheter),
      []).filter(
      ve => ve.id === hsaId)[0];


    fakeDatabase.anvandare.unitContext = newVardenhet;


    return fakeDatabase.anvandare;

  });