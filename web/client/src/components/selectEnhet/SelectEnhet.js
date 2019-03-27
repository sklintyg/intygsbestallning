import React from 'react';
import styled from 'styled-components'
import * as PropTypes from "prop-types";
import Vardgivare from "./Vardgivare";


const ComponentWrapper = styled.div`
  padding: 8px;
  max-width: 500px;
  
`

function SelectEnhet(props) {
  const {authoritiesTree, unitContext, selectEnhet} = props;

  const handleSelect = (hsaid) => () => selectEnhet(hsaid);

  return (
    <ComponentWrapper>
      {authoritiesTree.map(vg => {
          return (<Vardgivare key={vg.id}
                              vg={vg}
                              initiallyExpanded={true}
                              unitContext={unitContext}
                              handleSelect={handleSelect} />
          );
        }
      )}
    </ComponentWrapper>
  )
}

SelectEnhet.propTypes = {
  authoritiesTree: PropTypes.array,
  unitContext: PropTypes.object,
  selectEnhet: PropTypes.func
}

export default SelectEnhet;
