import React from 'react';
import {FlexColumnContainer, ScrollingContainer, WorkareaContainer} from "../components/layout/body";
import SelectEnhet from "../components/selectEnhet";

const SelectEnhetPage = () => {
  return (
    <FlexColumnContainer>
      <ScrollingContainer>
        <WorkareaContainer>
          <h1>Välj enhet</h1>
          <p>Du har behörighet för flera vårdenheter. Välj den du vill logga in på nu. Du kan byta enhet även efter
            inloggning.</p>
          <SelectEnhet />
        </WorkareaContainer>
      </ScrollingContainer>
    </FlexColumnContainer>
  )
};

export default SelectEnhetPage;
