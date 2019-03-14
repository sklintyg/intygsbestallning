import React, {Component} from "react";
import styled from 'styled-components/macro'

const Wrapper = styled.div`
  padding-top: 10px;
`;
class BestallningarFilter extends Component {
  render() {
    let value = "";
    return (
      <Wrapper>
        <div>Filter</div>
        <input ref={value} />
      </Wrapper>
    );
  }
}

export default BestallningarFilter;
