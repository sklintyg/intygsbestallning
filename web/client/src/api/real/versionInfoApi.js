import * as util from "./utils";

export const fetchVersionInfo = () => util.makeServerRequest('metrics/version', { pathComplete: true });
