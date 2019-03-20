import React from 'react';
import Styled from 'styled-components';
import * as Resources from './resources';

const Fraga = Styled.div`
  border: 1px solid lightgrey;
  border-radius: 5px;
  padding: 0 0 20px;
  margin: 10px 0;
`;

const Rubrik = Styled.h2`
  margin: 0;
  padding: 15px 30px 10px;
  border-bottom: 1px solid lightgrey;
`;

const Content = Styled.div`
  padding: 0 30px;
`

const Text = Styled.span`
  white-space: pre-line;
`;

const BestallningFraga = ({ props }) => (
  <Fraga>
    <Rubrik>{props.rubrik}</Rubrik>
    {props.delfragor.map((c, i) => (
      <Content key={i}>
        <h3>{c.etikett}</h3>
        {c.text ? <Text>{c.text}</Text> : null}
        {c.bild ? <img src={Resources[c.bild]} alt='none'/> : null}
      </Content>
    ))}
  </Fraga>
);

export default BestallningFraga;