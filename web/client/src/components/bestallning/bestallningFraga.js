import React from 'react';
import Styled from 'styled-components';

const Fraga = Styled.div`
  border: 1px solid lightgrey;
  padding: 10px;
  margin: 10px 0;
`;

const Rubrik = Styled.h2`
  margin: 0;
`;

const Text = Styled.span`
  white-space: pre-line;
`;

const BestallningFraga = ({ props }) => (
  <Fraga>
    <Rubrik>{props.rubrik}</Rubrik>
    {props.delfragor.map((c, i) => (
      <span key={i}>
        <h3>{c.etikett}</h3>
        <Text>{c.text}</Text>
      </span>
    ))}
  </Fraga>
);

export default BestallningFraga;