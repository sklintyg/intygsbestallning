import React from 'react';
import BodyCenterWrapper from "../components/layout/body";
import SelectEnhet from "../components/selectEnhet";

const SelectEnhetPage = () => {
    return (
      <BodyCenterWrapper>
          <h1>Välj enhet</h1>
          <p>Du har behörighet för flera vårdenheter. Välj den du vill logga in på nu. Du kan byta enhet även efter inloggning.</p>
          <SelectEnhet/>
      </BodyCenterWrapper>
    )
};

export default SelectEnhetPage;
