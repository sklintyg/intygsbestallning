import React from 'react'
import Styled from 'styled-components'
import * as Resources from './resources'
import Colors from '../styles/IbColors'
import * as Typo from '../styles/IbTypography'

const Fraga = Styled.div`
  width: 756px;
  border-radius: 4px;
  padding: 0 0 30px;
  margin: 10px auto;
  background: ${Colors.IB_COLOR_00};
`;

const Rubrik = Styled(Typo.IB_TYPO_02)`
  margin: 0;
  padding: 20px 30px 15px;
  border-bottom: 1px solid ${Colors.IB_COLOR_15};
  border-radius: 4px 4px 0 0;
  color: ${Colors.IB_COLOR_07};
`;

const Content = Styled.div`
  padding: 0 30px;
`

const Etikett = Styled(Typo.IB_TYPO_06)`
  padding: 20px 0 10px;
  color: ${Colors.IB_COLOR_09};
`

const Text = Styled(Typo.IB_TYPO_07)`
  white-space: pre-line;
  background: ${Colors.IB_COLOR_28};
  color: ${Colors.IB_COLOR_07};
  border-radius: 4px;
  padding: 10px;
`;

const Svar = Styled(Typo.IB_TYPO_07)`
  white-space: pre-line;
  background: ${Colors.IB_COLOR_15};
  color: ${Colors.IB_COLOR_07};
  border-radius: 4px;
  padding: 10px;
`;

const BestallningFraga = ({ props }) => (
  <Fraga>
    <Rubrik>{props.rubrik}</Rubrik>
    {props.delfragor.map((c, i) => (
      <Content key={i}>
        <Etikett>{c.etikett}</Etikett>
        {c.text ? <Text>{c.text}</Text> : null}
        {c.svar ? <Svar>{c.svar}</Svar> : null}
        {c.bild ? <img src={Resources[c.bild]} alt='none'/> : null}
      </Content>
    ))}
  </Fraga>
);

export default BestallningFraga;