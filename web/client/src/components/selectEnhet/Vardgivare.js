import React, {useState} from 'react';
import * as PropTypes from "prop-types";
import styled from "styled-components";
import {IbTypo05, IbTypo06} from "../styles/IbTypography";
import IbColors from "../styles/IbColors";
import Toggler from "../toggler/Toggler";
import {Button} from "reactstrap";

const ComponentWrapper = styled.div`
  padding-bottom: 4px;
`

const VardgivarTitle = styled(IbTypo05)`
  color: ${IbColors.IB_COLOR_09}
  background: ${IbColors.IB_COLOR_20}
  padding-left: 16px;
  display: flex;
  align-items: center;
  span {
    flex:1 0 auto;
  }
  margin-top: 10px;
`

const Vardenhet = styled(IbTypo06)`
  background: transparent;
  padding-left: 32px;
`

const Vardgivare = ({initiallyExpanded, vg, unitContext, handleSelect}) => {

  const [expanded, setExpanded] = useState(initiallyExpanded);

  const onToggleExpand = () => {
    setExpanded(!expanded);
  }

  const onSelect = (hsaid) => () => handleSelect(hsaid);

  return (
    <ComponentWrapper>
      <VardgivarTitle>
        <span>{vg.name}</span> <Toggler expanded={expanded} handleToggle={onToggleExpand} />
      </VardgivarTitle>
      {expanded && vg.vardenheter.map(ve => {
        const activeEnhet = (unitContext && unitContext.id === ve.id);

        return (
          <Vardenhet key={ve.id}>
            <Button
              color="link"
              onClick={onSelect(ve.id)}
              disabled={activeEnhet}>
              {ve.name} {activeEnhet && <span>(nuvarande enhet)</span>}
            </Button>
          </Vardenhet>
        )
      })}
    </ComponentWrapper>
  )
}

Vardgivare.propTypes = {
  vg: PropTypes.object.isRequired,
  handleSelect: PropTypes.func.isRequired,
  initiallyExpanded: PropTypes.bool,
  unitContext: PropTypes.object
};

export default Vardgivare;
