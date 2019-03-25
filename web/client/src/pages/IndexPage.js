import React from 'react';
import landing from './landningssida-min.png'
import styled from 'styled-components'
import Footer from "../components/Footer/Footer";
import {IB_TYPO_02, TextNormal} from "../components/style/IbTypography";
import {FlexColumnContainer, ScrollingContainer, WorkareaContainer} from "../components/layout/body";
import ibValues from "../components/style/IbValues";
import {Col, Container, Row} from "reactstrap";
import LoginOptions from "../components/loginOptions/LoginOptionsContainer";
import IbAlert from "../components/alert/Alert";

const CustomScrollingContainer = styled(ScrollingContainer)`
  max-width: none;
`

const PageContainer = styled(WorkareaContainer)`
  margin: auto;
  width: 100%;
  max-width: ${ibValues.maxContentWidth};
  display: flex;
  flex-direction: row;
  min-height: calc(100vh - 290px);
  padding-bottom: 60px;
  
  img {
  width: 100%;
  padding-bottom: 20px;
  }
`


const Welcome = styled(IB_TYPO_02)`
 
`


const HomePage = () => {
  return (
    <FlexColumnContainer>
      <CustomScrollingContainer>
        <PageContainer>
          <Container>
            <Row>
              <Col xs="12" md="7"><img src={landing} alt="Landningssida med illustration av stetoskop" /></Col>
              <Col xs="12" md="5">
                <Welcome as="h1">Välkommen till Intygsbeställning!</Welcome>
                <TextNormal as="p">Intygsbeställning är en tjänst för att hantera förfrågningar och beställningar av
                  medicinska utlåtanden och intyg till vården.</TextNormal>
                <TextNormal as="p">För att logga in behöver du ett giltigt e-tjänstekort
                  (exempelvis SITHS-kort) samt behörighet att ta del av förfrågningar och beställningar för din
                  vårdenhet. </TextNormal>

                <IbAlert type="info">De förfrågningar och beställningar som hanteras i Intygsbeställning
                  är journalhandlingar och all aktivitet i tjänsten loggas i enlighet med Patientdatalagen.
                </IbAlert>
                <LoginOptions/>
              </Col>
            </Row>
          </Container>
        </PageContainer>
        <Footer />
      </CustomScrollingContainer>

    </FlexColumnContainer>
  )
};

export default HomePage;
