import React, {Component} from 'react';
import * as PropTypes from "prop-types";
import styled from "styled-components";
import {IB_TYPO_05, IB_TYPO_06} from "../style/IbTypography";
import IbColors from "../style/IbColors";
import Toggler from "../toggler/Toggler";
import {Button} from "reactstrap";

const ComponentWrapper = styled.div`
  padding-bottom: 4px;
  
`

const VardgivarTitle = styled(IB_TYPO_05)`
  color: ${IbColors.IB_COLOR_09}
  background: ${IbColors.IB_COLOR_20}
  padding-left: 16px;
  display: flex;
  align-items: center;
  span {
    flex:1 0 auto;
  }
`

const Vardenhet = styled(IB_TYPO_06)`
  background: transparent;
  //padding: 8px 8px 8px 30px
  padding-left: 32px;
  
`


class Vardgivare extends Component {

  constructor(props) {
    super(props);
    this.state = {
      expanded: props.initiallyExpanded
    };

    this.onToggleExpand = this.onToggleExpand.bind(this);
  }

  onToggleExpand() {
    this.setState({expanded: !this.state.expanded});
  }

  render() {
    const {vg, currentVardenhet, handleSelect} = this.props;


    return (
      <ComponentWrapper>
        <VardgivarTitle>
          <span>{vg.namn}</span> <Toggler expanded={this.state.expanded} handleToggle={this.onToggleExpand} />
        </VardgivarTitle>
        {this.state.expanded && vg.vardenheter.map(ve => {
          return (
            <Vardenhet key={ve.id}>
              <Button color="link" onClick={handleSelect(ve.id)}
                      disabled={(currentVardenhet && currentVardenhet.id === ve.id)}>{ve.namn} {
                (currentVardenhet && currentVardenhet.id === ve.id) &&
                <span>(nuvarande enhet)</span>}
              </Button>
            </Vardenhet>

          )
        })}
      </ComponentWrapper>
    )

  }
}

Vardgivare.propTypes = {
  vg: PropTypes.object,
  initiallyExpanded: PropTypes.bool,
  currentVardenhet: PropTypes.object,
  selectEnhet: PropTypes.func
};

export default Vardgivare;
