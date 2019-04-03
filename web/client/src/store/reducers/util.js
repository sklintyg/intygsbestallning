import { haveMessage, getMessage } from "../../messages/messages";

export const buildClientError = (errorResponse, prefix) => {

  const {error} = errorResponse;
  let messageKey;

  if (!error || !error.hasOwnProperty('errorCode')) {
    messageKey = 'error.common.unknown';
  } else {
    const errorKey = error.errorCode.toLowerCase();
    messageKey = `${prefix}.${errorKey}`

    if (!haveMessage(messageKey)) {
      messageKey = `error.common.${errorKey}`;
      if (!haveMessage(messageKey)) {
        messageKey = `error.common.unknown`;
      }
    }
  }

  return {
    message: getMessage(messageKey)
  }
}
