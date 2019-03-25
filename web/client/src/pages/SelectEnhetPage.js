import React from 'react';
import {FlexColumnContainer, ScrollingContainer, WorkareaContainer} from "../components/styles/ibLayout";
import SelectEnhet from "../components/selectEnhet";
import Footer from "../components/Footer/Footer";
import styled from "styled-components";
import ibValues from "../components/styles/IbValues";

const CustomScrollingContainer = styled(ScrollingContainer)`
  max-width: none;
`
const PageContainer = styled(WorkareaContainer)`
  margin: auto;
  width: 100%;
  max-width: ${ibValues.maxContentWidth};
  display: flex;
  flex-direction: column;
  min-height: calc(100vh - 290px);
  padding-bottom: 60px;
  
  img {
  width: 100%;
  padding-bottom: 20px;
  }
`
const SelectEnhetPage = () => {
  return (
    <FlexColumnContainer>
      <CustomScrollingContainer>
        <PageContainer>
          <h1>Välj enhet</h1>
          <p>Du har behörighet för flera vårdenheter. Välj den du vill logga in på nu. Du kan byta enhet även efter
            inloggning.</p>
          <SelectEnhet />
        </PageContainer>
        <Footer />
      </CustomScrollingContainer>
    </FlexColumnContainer>
  )
};

export default SelectEnhetPage;
