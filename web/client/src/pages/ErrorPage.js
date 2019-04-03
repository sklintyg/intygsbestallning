import React from 'react';
import {FlexColumnContainer, ScrollingContainer, WorkareaContainer} from "../components/styles/ibLayout";
import AppFooter from "../components/appFooter/AppFooter";
import styled from "styled-components";
import {ErrorPageIcon} from "../components/styles/IbSvgIcons";
import IbAlert from "../components/alert/Alert";

const CustomScrollingContainer = styled(ScrollingContainer)`
  max-width: none;
`
const PageContainer = styled(WorkareaContainer)`
  margin: auto;
  width: 100%;
  max-width: 500px;
  display: flex;
  flex-direction: column;
  min-height: calc(100vh - 290px);
  padding-bottom: 60px;
  justify-content: center;
  align-items: center;

`
const ErrorPage = ({match}) => {
  let toShow = {title: 'unknown', message: 'unknown'};
  switch (match.params.errorCode) {
  case 'login.hsaerror':
    toShow = {title: 'Inloggning misslyckades', message: 'Det uppstod ett tekniskt fel när din behörighet skulle kontrolleras.'};
    break;
  case 'login.medarbetaruppdrag':
    toShow = {title: 'Inloggning misslyckades', message: 'Kontrollera att den länk du använder är korrekt.'};
    break;
  case 'login.failed':
    toShow = {
      title: 'Inloggning misslyckades',
      message: 'Ett fel uppstod vid inloggning. Vänligen kontrollera att du matat in rätt uppgifter.'
    };
    break;
  case 'UNAUTHORIZED':
    toShow = {
      title: 'Behörighet saknas',
      message: 'Du saknar behörighet för att ta del av den aktuella beställningen. För att ta del av en beställning krävs ett medarbetaruppdrag för vård och behandling på den vårdenhet dit beställningen inkommit. Om du anser dig ha behörighet att se beställningen, kontrollera att du har valt rätt vårdenhet och försök sedan att öppna beställningen igen.'
    };
    break;
  case 'NOT_FOUND':
    toShow = {title: 'Sidan du söker finns inte', message: 'Kontrollera att den länk du använder är korrekt.'};
    break;
  default:
    toShow = {
      title: 'Tekniskt fel',
      message: 'Ett fel uppstod.'
    };
  }
  return (
    <FlexColumnContainer>
      <CustomScrollingContainer>
        <PageContainer>
          <ErrorPageIcon />
          <br/>
          <h2>{toShow.title}</h2>
          <IbAlert type="error">{toShow.message}</IbAlert>
        </PageContainer>
        <AppFooter />
      </CustomScrollingContainer>
    </FlexColumnContainer>
  )
};

export default ErrorPage;
