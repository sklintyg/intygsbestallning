import React from "react";
import styled from "styled-components/macro";
import {debounce} from 'lodash';
import PropTypes from "prop-types";

const Wrapper = styled.div`
  padding-top: 10px;
`;

const TextSearch = ({onChange}) => {
  const debounceHandleChange = debounce(value => {
    onChange(value)
  }, 1000);

  const handleChange = e => {
    debounceHandleChange(e.target.value); // perform a search only once every 1000ms
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

TextSearch.propTypes = {
  onChange: PropTypes.func.isRequired
}

export default TextSearch;
