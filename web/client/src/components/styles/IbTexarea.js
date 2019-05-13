import React, { useState } from 'react'
import * as PropTypes from 'prop-types'
import styled from 'styled-components'
import IbColors from '../styles/IbColors'

/**
 * This button implements IB Standard component IKA-007,
 * see https://inera-certificate.atlassian.net/wiki/spaces/IT/pages/900727193/I+-+IB+Inputkomponenter+standard#I-IBInputkomponenterstandard-7.Fritextf%C3%A4lt(expanderar)
 */
const lineHeight = 16
const StyledTextarea = styled.textarea`
  resize: none;
  font-size: 14px;
  line-height: ${lineHeight}px;
  overflow: auto;
  height: auto;
  padding: 8px;
  width: 100%;
  color: ${IbColors.IB_COLOR_17};

  }
`

const IbTextarea = ({ placeholder, minRows, maxRows, onChange, ...rest }) => {
  const [config, setConfig] = useState({
    value: '',
    rows: minRows,
  })
  const handleChange = (event) => {
    const previousRows = event.target.rows
    event.target.rows = minRows // reset number of rows in textarea

    const currentRows = ~~(event.target.scrollHeight / lineHeight) - 1
    if (currentRows === previousRows) {
      event.target.rows = currentRows
    }

    if (currentRows >= maxRows) {
      event.target.rows = maxRows
      event.target.scrollTop = event.target.scrollHeight
    }
    setConfig({
      value: event.target.value,
      rows: currentRows < maxRows ? currentRows : maxRows,
    })

    //Propagate new current value to parent component
    onChange(event.target.value)
  }

  return <StyledTextarea rows={config.rows} value={config.value} placeholder={placeholder} onChange={handleChange} {...rest} />
}

IbTextarea.propTypes = {
  placeholder: PropTypes.string,
  minRows: PropTypes.number,
  maxRows: PropTypes.number,
  onChange: PropTypes.func,
}

export default IbTextarea
