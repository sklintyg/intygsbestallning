import { delay } from './util'

export const fetchAppConfig = () =>
  delay(500).then(() => {
    return {
      versionInfo: { applicationName: 'ib', buildVersion: '0', buildTimestamp: 'now', activeProfiles: 'some,profiles' },
      loginUrl: 'LoginUrlFromServerConfig',
    }
  })
