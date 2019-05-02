import React from 'react'
import styled from 'styled-components'
import * as PropTypes from 'prop-types'
import {CollapseIcon, ExpandIcon} from '../styles/IbSvgIcons'

const TogglerTag = styled.button`
  padding: 0;
  border: none;
  background: transparent;
  &:focus {
    outline: none;
  }
`

const Toggler = ({ expanded, handleToggle, color }) => (
  <TogglerTag onClick={handleToggle}>{expanded ? <ExpandIcon color={color} /> : <CollapseIcon color={color} />}</TogglerTag>
)

Toggler.propTypes = {
  expanded: PropTypes.bool,
  handleToggle: PropTypes.func,
  color: PropTypes.string,
}

export default Toggler
