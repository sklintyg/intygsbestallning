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
`

const Vardenhet = styled(IbTypo06)`
  background: transparent;
  //padding: 8px 8px 8px 30px
  padding-left: 32px;
`

const Vardgivare = ({initiallyExpanded, vg, unitContext, handleSelect}) => {

  const [expanded, setExpanded] = useState(initiallyExpanded);

  const onToggleExpand = () => {
    setExpanded(!expanded);
  }

  return (
    <ComponentWrapper>
      <VardgivarTitle>
        <span>{vg.name}</span> <Toggler expanded={expanded} handleToggle={onToggleExpand} />
      </VardgivarTitle>
      {expanded && vg.vardenheter.map(ve => {
        return (
          <Vardenhet key={ve.id}>
            <Button color="link" onClick={handleSelect(ve.id)}
                    disabled={(unitContext && unitContext.id === ve.id)}>{ve.name} {
              (unitContext && unitContext.id === ve.id) &&
              <span>(nuvarande enhet)</span>}
            </Button>
          </Vardenhet>

        )
      })}
    </ComponentWrapper>
  )
}

Vardgivare.propTypes = {
  vg: PropTypes.object,
  initiallyExpanded: PropTypes.bool,
  unitContext: PropTypes.object,
  selectEnhet: PropTypes.func
};

export default Vardgivare;
