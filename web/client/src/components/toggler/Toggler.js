import React from 'react';
import styled from 'styled-components'
import * as PropTypes from "prop-types";


const TogglerTag = styled.a`
  padding: 4px;
  font-weight: bold;
  
`

//TODO: change to use icons for expanded state
const Toggler = ({expanded, handleToggle}) => (
    <TogglerTag onClick={handleToggle}>{expanded ? '-' : '+'} </TogglerTag>
  )


Toggler.propTypes = {
  expanded: PropTypes.bool,
  handleToggle: PropTypes.func
}

export default Toggler;
