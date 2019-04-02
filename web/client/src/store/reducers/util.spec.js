import *  as util from "./util";
import *  as messages from "../../messages/messages";

messages.getMessage = (messageKey) => messageKey;
messages.haveMessage = (messageKey) => {
  if (messageKey === 'error.user.not_found') {
    return true;
  }

  if (messageKey === 'error.common.network_error') {
    return true;
  }

  return false;
};

describe('util test', () => {

  describe('buildClientError', () => {

    it('empty error', () => {
      const errorResponse = {};

      const result = util.buildClientError(errorResponse, 'error.user');

      expect(result).toEqual({message: 'error.common.unknown'})
    });

    it('missing errorCode', () => {
      const errorResponse = {
        error: { }
      };

      const result = util.buildClientError(errorResponse, 'error.user');

      expect(result).toEqual({message: 'error.common.unknown'})
    });

    it('errorCode found', () => {
      const errorResponse = {
        error: {
          errorCode: 'NOT_FOUND'
        }
      };

      const result = util.buildClientError(errorResponse, 'error.user');

      expect(result).toEqual({message: 'error.user.not_found'})
    });

    it('errorCode fallback', () => {
      const errorResponse = {
        error: {
          errorCode: 'NETWORK_ERROR'
        }
      };

      const result = util.buildClientError(errorResponse, 'error.user');

      expect(result).toEqual({message: 'error.common.network_error'})
    });

    it('errorCode fallback common', () => {
      const errorResponse = {
        error: {
          errorCode: 'NOT_FOUND_CODE'
        }
      };

      const result = util.buildClientError(errorResponse, 'error.user');

      expect(result).toEqual({message: 'error.common.unknown'})
    });
  })
});
