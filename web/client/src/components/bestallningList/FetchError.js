import React from 'react';
import PropTypes from 'prop-types'

const FetchError = ({ message, onRetry }) => (
  <div>
    <p>Kunde inte hämta beställningar. Fel: {message}</p>
    <button onClick={onRetry}>Retry</button>
  </div>
);

FetchError.propTypes = {
  message: PropTypes.string.isRequired,
  onRetry: PropTypes.func.isRequired,
};

export default FetchError;
