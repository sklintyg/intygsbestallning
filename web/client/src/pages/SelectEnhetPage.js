import React from 'react';
import ValjEnhet from "../components/selectEnhet";
import BodyCenterWrapper from "../components/layout/body";

const SelectEnhetPage = () => {
    return (
      <BodyCenterWrapper>
          <h1>Välj enhet</h1>
          <p>Du har behörighet för flera vårdenheter. Välj den du vill logga in på nu. Du kan byta enhet även efter inloggning.</p>
          <ValjEnhet/>
      </BodyCenterWrapper>
    )
};

export default SelectEnhetPage;
