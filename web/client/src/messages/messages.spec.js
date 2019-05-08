import *  as messages from "./messages";

jest.mock('./messages.json', () => ({
  "error": {
    "common": {
      "bestallning_fel004_tekniskt_fel": "Unknown error"
    }
  }
}), { virtual: true })

describe('messages test', () => {

  describe('haveMessage', () => {
    it('missing message', () => {
      const result = messages.haveMessage('not.found.message.key');

      expect(result).toBeFalsy();
    });

    it('found message', () => {
      const result = messages.haveMessage('error.common.bestallning_fel004_tekniskt_fel');

      expect(result).toBeTruthy();
    })
  })

  describe('getMessage', () => {
    it('missing message', () => {
      const result = messages.getMessage('not.found.message.key');

      expect(result).toEqual('Missing: not.found.message.key');
    });

    it('found message', () => {
      const result = messages.getMessage('error.common.bestallning_fel004_tekniskt_fel');

      expect(result).toEqual('Unknown error');
    })
  })
});
