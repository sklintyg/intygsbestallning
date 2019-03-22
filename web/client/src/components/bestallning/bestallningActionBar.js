import React, {Fragment} from 'react'
import * as actions from '../../store/actions/bestallning'
import { connect } from 'react-redux'
import { Button } from 'reactstrap'
import { compose } from 'recompose'

const BestallningActionBar = ({bestallning, setStatus}) => {

  const accept = () => {
    setStatus(bestallning.id, 'ACCEPTED');
  }
  
  const reject = () => {
    setStatus(bestallning.id, 'REJECTED');
  }
  
  const complete = () => {
    setStatus(bestallning.id, 'COMPLETED');
  }

  return (
    <Fragment>
      { bestallning.status === 'LAST' ? <Button onClick={accept}>Acceptera</Button> : null }
      { bestallning.status === 'LAST' ? <Button onClick={reject}>Avvisa</Button> : null }
      { bestallning.status === 'ACCEPTERAD' ? <Button outline={true} onClick={complete}>Klarmakera</Button> : null }
      <Button>Vidarebefodra</Button>
      <Button>Skriv ut</Button>
    </Fragment>
  )
};

export default compose(
  connect(null, actions)
)(BestallningActionBar);