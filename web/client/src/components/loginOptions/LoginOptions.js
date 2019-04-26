import React from 'react'
import {Section} from '../styles/ibLayout'
import IbButton6 from '../styles/IbButton6'

const LoginOptions = ({settings, isFetching, error}) => {

  const doLogin = (loginUrl) => () => {
    window.location.href = loginUrl
  }
  return (
      <Section>
        {(!isFetching && settings) && <IbButton6 onClick={doLogin(settings.loginUrl)} label="Logga in med SITHS-kort"/> }
      </Section>
  )
}

export default LoginOptions
