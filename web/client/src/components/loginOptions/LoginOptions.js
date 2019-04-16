import React, { Fragment } from 'react'
import * as PropTypes from 'prop-types'
import { Button } from 'reactstrap'
import { Section } from '../styles/ibLayout'
import { NavigateNext } from '../styles/IbSvgIcons';

function LoginOptions(props) {
  const { selectLoginType } = props

  const handleSelect = (loginType) => () => selectLoginType(loginType)

  return (
    <Fragment>
      <Section>
        <Button color="primary" outline={true} onClick={handleSelect('siths')}>
          Logga in med SITHS-kort <NavigateNext></NavigateNext>
        </Button>
      </Section>
      <Section>
        <Button color="primary" outline={true} onClick={handleSelect('sambi')}>
          Logga in via Sambi <NavigateNext></NavigateNext>
        </Button>
      </Section>
    </Fragment>
  )
}

LoginOptions.propTypes = {
  selectLoginType: PropTypes.func,
}

export default LoginOptions
