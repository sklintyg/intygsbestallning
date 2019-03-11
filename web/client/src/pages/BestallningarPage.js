import React from 'react';
import PropTypes from 'prop-types'

import BestallningarFilter from '../components/Bestallningar/BestallningarFilter';
import BestallningarContainer from '../components/Bestallningar/BestallningarContainer';

const BestallningarPage = () => {
    return (
        <div>
            <div>Container wooh: </div>
            <BestallningarFilter />
            <BestallningarContainer />
        </div>
    )
};

BestallningarPage.propTypes = {
    params: PropTypes.shape({
      filter: PropTypes.string,
    }),
  };
  
export default BestallningarPage;