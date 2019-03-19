import React, { Component } from "react";
import styled from "styled-components/macro";
import { Debounce } from "react-throttle";

const Wrapper = styled.div`
  padding-top: 10px;
`;

class TextSearch extends Component {
  handleChange = e => {
    this.props.onChange(e.target.value); // perform a search only once every 200ms
  };

  render() {
    return (
      <Wrapper>
        <label htmlFor="textFilter">Filter</label>
        <div>
          <Debounce time="1000" handler="onChange">
            <input id="textFilter" type="text" onChange={this.handleChange} />
          </Debounce>
        </div>
      </Wrapper>
    );
  }
}

export default TextSearch;
