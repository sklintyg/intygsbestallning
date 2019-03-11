import React from 'react';
import PropTypes from 'prop-types'
import {Link} from 'react-router-dom';

const BestallningarList = ({bestallningar}) => {
    return (
      <div>
        <table>
          <tbody>
            {bestallningar.map((bestallning) => 
              <tr key={bestallning.id}>
                <td>{bestallning.name}</td>
                <td>
                  <Link to={`bestallning/${bestallning.id}`}>
                    Visa
                  </Link>
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    )
  }

BestallningarList.propTypes = {
  bestallningar: PropTypes.arrayOf(PropTypes.shape({
    id: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired
  }))
};

export default BestallningarList;
