import * as util from "./utils";

export const fetchVersionInfo = () => util.makeServerRequest('public-api/version', { pathComplete: true });
