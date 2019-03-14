import React from "react";
import { NavLink } from "react-router-dom";
import styled from 'styled-components/macro'

const Wrapper = styled.div`
  & > a {
    padding-right: 10px;
  }
`;

const BestallningFilterBar = () => {
  return (
    <Wrapper>
      <NavLink to={`/bestallningar/active`}>Aktiva</NavLink>
      <NavLink to={`/bestallningar/completed`}>Klara</NavLink>
      <NavLink to={`/bestallningar/rejected`}>Avvisade</NavLink>
    </Wrapper>
  );
};

export default BestallningFilterBar;
