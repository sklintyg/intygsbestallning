import React from 'react'
import styled from 'styled-components'
import PropTypes from 'prop-types'
import Vardgivare from './Vardgivare'
import IbAlert, { alertType } from '../alert/Alert'
import ErrorMessageFormatter from '../../messages/ErrorMessageFormatter'
import LoadingSpinner from '../loadingSpinner'

const SelectEnhetSpinnerWrapper = styled.div`
  position: absolute;
  z-index: 2000;
  background-color: rgb(255, 255, 255, 0.5);

  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
`

const ComponentWrapper = styled.div`
  padding: 8px;
  max-width: 500px;
`

function SelectEnhet({ authoritiesTree, unitContext, activeError, selectEnhet, setEnhetPending }) {
  return (
    <ComponentWrapper>
      <fieldset disabled={setEnhetPending}>
        {setEnhetPending && (
          <SelectEnhetSpinnerWrapper>
            <LoadingSpinner message={'Laddar enhet'} loading={setEnhetPending} color="primary"/>
          </SelectEnhetSpinnerWrapper>
        )}
        {activeError && (
          <IbAlert type={alertType.ERROR}>
            <ErrorMessageFormatter error={activeError} />
          </IbAlert>
        )}
        {authoritiesTree.map((vg) => {
          return <Vardgivare key={vg.id} vg={vg} initiallyExpanded={true} unitContext={unitContext} handleSelect={selectEnhet} />
        })}
      </fieldset>
    </ComponentWrapper>
  )
}

SelectEnhet.propTypes = {
  authoritiesTree: PropTypes.array.isRequired,
  selectEnhet: PropTypes.func.isRequired,
  unitContext: PropTypes.object,
  activeError: PropTypes.object,
}

export default SelectEnhet
