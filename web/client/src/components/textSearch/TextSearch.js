import React from "react";
import styled from "styled-components/macro";
import {debounce} from 'lodash';

const Wrapper = styled.div`
  padding-top: 10px;
`;

const TextSearch = ({onChange}) => {
  const debounceHandleChange = debounce(value => {
    onChange(value.target.value)
  }, 1000);

  const handleChange = e => {
    debounceHandleChange(e.target.value); // perform a search only once every 200ms
  }

  return (
    <Wrapper>
      <label htmlFor="textFilter">Filter</label>
      <div>
        <input id="textFilter" type="text" onChange={handleChange} />
      </div>
    </Wrapper>
  );
}

export default TextSearch;
