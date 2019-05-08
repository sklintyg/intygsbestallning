import React from 'react'
import { Section } from '../styles/ibLayout'
import IbButton6 from '../styles/IbButton6'
import { Spinner } from 'reactstrap'
import ErrorMessageFormatter from '../../messages/ErrorMessageFormatter'
import IbAlert, { alertType } from '../alert/Alert'

const LoginOptions = ({ settings, isFetching, errorMessage }) => {
  const doLogin = (loginUrl) => () => {
    window.location.href = loginUrl
  }
  return (
    <Section>
      {isFetching && (
        <span>
          <Spinner size="sm" color="secondary" /> Hämtar konfiguration...{' '}
        </span>
      )}
      {!isFetching && !errorMessage && <IbButton6 id="loginSithsBtn" onClick={doLogin(settings.loginUrl)} label="Logga in med SITHS-kort" />}
      {errorMessage && (
        <IbAlert type={alertType.ERROR}>
          {' '}
          <ErrorMessageFormatter error={errorMessage} />
        </IbAlert>
      )}
    </Section>
  )
}

export default LoginOptions
