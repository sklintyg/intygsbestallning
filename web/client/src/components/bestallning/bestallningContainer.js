import React, {Component} from 'react';
import {withRouter} from 'react-router-dom';
import {connect} from 'react-redux';
import * as actions from '../../store/actions/bestallning';
import {getBestallning, getErrorMessage} from '../../store/reducers/bestallning';
import BestallningFraga from './bestallningFraga';
import BestallningActionBar from './bestallningActionBar';
import {FlexColumnContainer, ScrollingContainer, WorkareaContainer} from "../layout/body";
import Footer from "../Footer/Footer";
import styled from "styled-components";
import Colors from '../style/IbColors';
import ibValues from '../style/IbValues';

const CustomScrollingContainer = styled(ScrollingContainer)`
  background-color:${Colors.IB_COLOR_27};
  max-width: none;
`
const CustWorkareaContainer = styled.div`
  margin: auto;
  width: 100%;
  max-width: ${ibValues.maxContentWidth};
  padding: 10px 30px 20px;
`
const SomeDiv = styled.div`
  box-shadow: 0px 5px 9px -6px #000;
  position: relative;
`

class BestallningContainer extends Component {

  componentDidMount() {
    this.fetchData();
  }

  componentDidUpdate(prevProps) {
    if (this.props.id !== prevProps.id) {
      this.fetchData();
    }
  }

  fetchData() {
    const {fetchBestallning, id} = this.props;
    fetchBestallning(id);
  }

  render() {
    const {errorMessage, bestallning, history} = this.props;
    const bestallningIsEmpty = Object.entries(bestallning).length === 0 && bestallning.constructor === Object;

    if (bestallningIsEmpty) {
      return <p>Loading...</p>;
    }
    if (errorMessage && bestallningIsEmpty) {
      return (
        <div>
          {errorMessage}
        </div>
      );
    }

    return (
      <FlexColumnContainer>
        <SomeDiv>
          <CustWorkareaContainer>
            <div>
              <span onClick={history.goBack}>Tillbaka till lista</span>
              <span> :: Förfrågan av Intygstyp '{bestallning.intygName}'</span>
              <span> :: Status {bestallning.status}</span>
              <span> :: Inkom {bestallning.ankomstDatum}</span>
            </div>
            <hr />
            <div>{bestallning.id}</div>
            <div>{bestallning.patient.id} - {bestallning.patient.namn}</div>
            <BestallningActionBar props={bestallning} />
          </CustWorkareaContainer>
        </SomeDiv>
        <CustomScrollingContainer>
          <WorkareaContainer>
            {bestallning.struktur.map((b, i) => <BestallningFraga key={i} props={b} />)}
          </WorkareaContainer>
          <Footer />
        </CustomScrollingContainer>
      </FlexColumnContainer>
    )
  }
};

const mapStateToProps = (state, {match, history}) => {
  const id = match.params.id;
  return {
    id,
    history,
    bestallning: getBestallning(state),
    errorMessage: getErrorMessage(state),
  };
};

BestallningContainer = withRouter(connect(
  mapStateToProps,
  actions,
)(BestallningContainer));

export default BestallningContainer;
