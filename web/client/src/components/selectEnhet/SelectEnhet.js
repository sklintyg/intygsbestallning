import React from 'react';
import styled from 'styled-components'
import IbColors from "../style/IbColors";
import {InternalLink, IB_TYPO_01, IB_TYPO_06} from "../style/IbTypography";


const ComponentWrapper = styled.div`
  padding: 8px;
  
`

const Vardgivare = styled(IB_TYPO_01)`
  color: ${IbColors.IB_COLOR_09}
  background: ${IbColors.IB_COLOR_20}
  padding: 8px 16px;
  
`
const Vardenhet = styled(IB_TYPO_06)`
  background: transparent
  padding: 8px 8px 8px 30px
  
`



const SelectEnhet = ({vardgivare, selectEnhet}) => {

  const handleSelect = (hsaid) => () => selectEnhet(hsaid);

  return (
    <ComponentWrapper>
      {vardgivare.map(vg => {
          return (
            <div key={vg.id + '-wrapper'}>
              <Vardgivare as="h2" key={vg.id}> {vg.namn}</Vardgivare>
              {
                vg.vardenheter.map(ve => {
                  return (
                    <Vardenhet key={ve.id}>
                      <InternalLink onClick={handleSelect(ve.id)}>{ve.namn}</InternalLink>
                    </Vardenhet>
                  )
                })
              }
            </div>
          );
        }
      )}
    </ComponentWrapper>
  )
};

export default SelectEnhet;
