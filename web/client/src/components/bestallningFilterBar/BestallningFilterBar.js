import React from 'react';
import { NavLink } from 'react-router-dom';
import styled from 'styled-components/macro'

const Wrapper = styled.div`
  & > a {
    padding-right: 10px;
  }
`;

const BestallningFilterBar = () => {
  return (
    <Wrapper>
      <NavLink to={`/bestallningar/AKTUELLA`}>Aktiva</NavLink>
      <NavLink to={`/bestallningar/KLARA`}>Klara</NavLink>
      <NavLink to={`/bestallningar/AVVISADE`}>Avvisade</NavLink>
    </Wrapper>
  );
};

export default BestallningFilterBar;
