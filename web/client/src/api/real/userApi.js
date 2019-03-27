import * as util from "./utils";

export const fetchAnvandare = () => util.makeServerRequest('anvandare');

export const logoutUser = () => util.makeServerRequest('anvandare/logout');

export const changeEnhet = (hsaId) => util.makeServerPost('anvandare/unit-context/' + hsaId);
