import { v4 } from 'node-uuid';

// This is a fake in-memory implementation of something
// that would be implemented by calling a REST server.

const fakeDatabase = {
  bestallningar: [{
    id: v4(),
    text: 'hey',
    completed: true,
  }, {
    id: v4(),
    text: 'ho',
    completed: true,
  }, {
    id: v4(),
    text: 'let’s go',
    completed: false,
  }],
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
