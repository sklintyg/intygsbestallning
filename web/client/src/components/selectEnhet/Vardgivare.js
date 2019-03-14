import React, {Component} from 'react';
import * as PropTypes from "prop-types";
import styled from "styled-components";
import {IB_TYPO_01, IB_TYPO_06, InternalLink} from "../style/IbTypography";
import IbColors from "../style/IbColors";
import Toggler from "../toggler/Toggler";


const VardgivarTitle = styled(IB_TYPO_01)`
  color: ${IbColors.IB_COLOR_09}
  background: ${IbColors.IB_COLOR_20}
  padding: 8px 16px;
  
`

const Vardenhet = styled(IB_TYPO_06)`
  background: transparent
  padding: 8px 8px 8px 30px
  
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
      <React.Fragment>
        <VardgivarTitle>
          <Toggler expanded={this.state.expanded} handleToggle={this.onToggleExpand} />
          {vg.namn}
        </VardgivarTitle>
        {this.state.expanded && vg.vardenheter.map(ve => {
          return (
            <Vardenhet key={ve.id}>
              {(currentVardenhet && currentVardenhet.id === ve.id) ?
                <span>{ve.namn} (nuvarande enhet)</span> : <InternalLink onClick={handleSelect(ve.id)}>{ve.namn}</InternalLink>

              }
            </Vardenhet>
          )
        })}
      </React.Fragment>
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
