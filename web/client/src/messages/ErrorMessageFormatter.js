import React, {Fragment} from 'react';
import PropTypes from "prop-types";
import styled from 'styled-components'

const LogIdFooter = styled.div`
  padding-top: 8px;
`

const ErrorMessageFormatter = (props) => {
  const {title, message, logId} = props.error
  console.log(title);
  return (
    <Fragment>
      {title && <h5>{title}</h5>}
      {message && <div>{message}</div>}
      {logId && <LogIdFooter>Om problemet kvarstår, kontakta i första hand din lokala IT-avdelning och i
        andra hand <a href="https://www.inera.se/felanmalan" target="_blank" rel="noopener noreferrer">Inera kundservice</a> och uppge då följande logg id: {logId}</LogIdFooter>}
    </Fragment>
  );
}

ErrorMessageFormatter.propTypes = {
  error: PropTypes.object.isRequired
}

export default ErrorMessageFormatter;
