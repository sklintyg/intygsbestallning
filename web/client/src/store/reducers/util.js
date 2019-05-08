import {getMessage, haveMessage} from "../../messages/messages";

export const buildClientError = (errorResponse, prefix) => {

  const {error} = errorResponse;
  let titleKey, messageKey, logId;

  if (!error || !error.hasOwnProperty('errorCode')) {
    messageKey = 'error.common.bestallning_fel004_tekniskt_fel.message';
    titleKey = 'error.common.bestallning_fel004_tekniskt_fel.title';
    logId = null;
  } else {
    const errorKey = error.errorCode.toLowerCase();
    logId = error.logId || null;

    titleKey = `${prefix}.${errorKey}.title`
    messageKey = `${prefix}.${errorKey}.message`

    if (!haveMessage(messageKey)) {
      titleKey = `error.common.${errorKey}.title`;
      messageKey = `error.common.${errorKey}.message`;
      if (!haveMessage(messageKey)) {
        titleKey = `error.common.bestallning_fel004_tekniskt_fel.title`;
        messageKey = `error.common.bestallning_fel004_tekniskt_fel.message`;
      }
    }
  }


  return {
    title: getMessage(titleKey),
    message: getMessage(messageKey),
    logId : logId
  }
}
