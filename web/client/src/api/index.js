import {v4} from 'node-uuid';
import anvandare from "./anvandare";

// This is a fake in-memory implementation of something
// that would be implemented by calling a REST server.

const fakeDatabase = {
  bestallningar: [{
    id: v4(),
    name: 'hey',
    completed: true,
  }, {
    id: v4(),
    name: 'ho',
    completed: true,
  }, {
    id: v4(),
    name: 'let’s go',
    completed: false,
  }],
  anvandare: anvandare
};

const delay = (ms) =>
  new Promise(resolve => setTimeout(resolve, ms));

export const fetchBestallningar = (filter) =>
  delay(500).then(() => {
    switch (filter) {
      case 'all':
        return fakeDatabase.bestallningar;
      case 'active':
        return fakeDatabase.bestallningar.filter(t => !t.completed);
      case 'completed':
        return fakeDatabase.bestallningar.filter(t => t.completed);
      default:
        throw new Error(`Unknown filter: ${filter}`);
    }
  });

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




