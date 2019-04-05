import React, { useState, useEffect } from 'react'
import styled from 'styled-components'
import { Spinner } from 'reactstrap'

const SpinnerBox = styled.div`
  width: 100%;
  text-align: center;
  margin-top: auto;
  margin-bottom: auto;
`

const SpinnerWrapper = styled.div`
  background-color: rgba(255, 255, 255, 0.5);
  position: absolute;
  top: 0;
  width: 100%;
  height: 100%;
  display: flex;
`

const LoadingSpinner = ({ loading, message }) => {
  const [showSpinner, setShowSpinner] = useState(false)

  useEffect(() => {
    let timeout
    if (loading && !showSpinner) {
      timeout = setTimeout(() => setShowSpinner(true), 500)
    }
    return () => clearInterval(timeout)
  })

  if (loading) {
    if (!showSpinner) {
      return null
    }
    return (
      <SpinnerWrapper>
        <SpinnerBox>
          <Spinner color="secondary" />
          <div>{message}</div>
        </SpinnerBox>
      </SpinnerWrapper>
    )
  }
}

export default LoadingSpinner
