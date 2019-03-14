import React from 'react';
import styled from 'styled-components'
import * as PropTypes from "prop-types";
import Vardgivare from "./Vardgivare";


const ComponentWrapper = styled.div`
  padding: 8px;
  max-width: 400px;
  
`

function SelectEnhet(props) {
  const {vardgivare, currentVardenhet, selectEnhet} = props;

  const handleSelect = (hsaid) => () => selectEnhet(hsaid);

  return (
    <ComponentWrapper>
      {vardgivare.map(vg => {
          return (
            <React.Fragment key={vg.id}>
              <Vardgivare vg={vg} initiallyExpanded={true} currentVardenhet={currentVardenhet}
                          handleSelect={handleSelect} />
            </React.Fragment>
          );
        }
      )}
    </ComponentWrapper>
  )
}

SelectEnhet.propTypes = {
  vardgivare: PropTypes.array,
  currentVardenhet: PropTypes.object,
  selectEnhet: PropTypes.func
}

export default SelectEnhet;
