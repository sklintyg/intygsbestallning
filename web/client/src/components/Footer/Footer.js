import React from 'react';
import styled from 'styled-components'
import ibValues from "../style/IbValues";


const FooterOuterContainer = styled.div`
  min-height: 80px;
  background-color: #eee;
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
  padding: 10px 40px;
  h3 {
   font-size: 13px;
  }
  p {
    font-size: 12px;
  }
`


const Footer = () => (
  <FooterOuterContainer>
    <FooterContainer>
      <FooterSection>
        <h3 className="title">Intygsbeställning är en tjänst som drivs av Inera AB</h3>
        <p>Box 11703, 118 93 Stockholm</p>
        <p><a href="/">www.inera.se</a></p>
      </FooterSection>
      <FooterSection>
        <h3>Kundtjänst</h3>
        <p>Tel 0771 - 25 10 10</p>
        <p><a href="/">Kontakt och support</a></p>
      </FooterSection>
      <FooterSection>
        <h3>Läs mer om inloggningen</h3>
        <p><a href="/">SITHS-kort</a></p>
        <p><a href="/">Sambi</a></p>

      </FooterSection>
    </FooterContainer>
  </FooterOuterContainer>
)


export default Footer;
