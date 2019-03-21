import React from 'react';
import landing from './landningssida-min.png'
import styled from 'styled-components'
import Footer from "../components/Footer/Footer";
import {IB_TYPO_02} from "../components/style/IbTypography";
import {FlexColumnContainer, ScrollingContainer, WorkareaContainer} from "../components/layout/body";
import ibValues from "../components/style/IbValues";

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
  
  section {
  padding-right: 30px;
  }
`

//TODO - create shared component or use bootstrap?
const AlertInfo = styled.div`
color: #004085;
    background-color: #cce5ff;
    border-color: #b8daff;
        position: relative;
    padding: .75rem 1.25rem;
    margin-bottom: 1rem;
    border: 1px solid transparent;
    border-radius: .25rem;
`
const Welcome = styled(IB_TYPO_02)`
 
`


const HomePage = () => {
  return (
    <FlexColumnContainer>
      <CustomScrollingContainer>
        <PageContainer>
          <section><img src={landing} alt="Landningssida med illustration av stetoskop" /></section>
          <section>
            <Welcome as="h1">Välkommen till Intygsbeställning!</Welcome>
            <p>Intygsbeställning är en tjänst för att hantera förfrågningar och beställningar av medicinska utlåtanden
              och intyg till vården.

              För att logga in behöver du ett giltigt e-tjänstekort (exempelvis SITHS-kort) samt behörighet att ta del
              av förfrågningar och beställningar för din vårdenhet. </p>

            <AlertInfo>De förfrågningar och beställningar som hanteras i Intygsbeställning
              är journalhandlingar och all aktivitet i tjänsten loggas i enlighet med Patientdatalagen.</AlertInfo>
            <button>Logga in</button>
          </section>
        </PageContainer>
        <Footer />
      </CustomScrollingContainer>

    </FlexColumnContainer>
  )
};

export default HomePage;
