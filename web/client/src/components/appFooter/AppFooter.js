import React from 'react';
import styled from 'styled-components'
import ibValues from "../styles/IbValues";
import ibColors from "../styles/IbColors";
import {IbTypo06, IbTypo07} from "../styles/IbTypography";


const FooterOuterContainer = styled.div`
  min-height: 80px;
  background-color: ${ibColors.IB_COLOR_28};
  box-shadow: inset 1px 4px 9px -6px #000;
  width: 100%;
  padding: 38px 10px;
 
`
const FooterContainer = styled.div`
  min-height: 80px;
  display: flex;
  flex-direction: row;
  
  margin: auto;
  width: 100%;
  max-width: ${ibValues.maxContentWidth};
`
const FooterSection = styled.section`
  padding: 10px 40px 10px 10px;
`


const AppFooter = () => (
  <FooterOuterContainer>
    <FooterContainer>
      <FooterSection>
        <IbTypo06 as="h3">Intygsbeställning är en tjänst som drivs av Inera AB</IbTypo06>
        <IbTypo07 as="p">Box 11703, 118 93 Stockholm</IbTypo07>
        <IbTypo07 as="p"><a href="/">www.inera.se</a></IbTypo07>
      </FooterSection>
      <FooterSection>
        <IbTypo06>Kundtjänst</IbTypo06>
        <IbTypo07 as="p">Tel 0771 - 25 10 10</IbTypo07>
        <IbTypo07 as="p"><a href="/">Kontakt och support</a></IbTypo07>
      </FooterSection>
      <FooterSection>
        <IbTypo06>Läs mer om inloggningen</IbTypo06>
        <IbTypo07 as="p"><a href="/">SITHS-kort</a></IbTypo07>
        <IbTypo07 as="p"><a href="/">Sambi</a></IbTypo07>

      </FooterSection>
    </FooterContainer>
  </FooterOuterContainer>
)


export default AppFooter;
