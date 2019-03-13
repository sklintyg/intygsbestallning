import React from "react";
import PropTypes from "prop-types";
import { Link } from "react-router-dom";

const BestallningarList = ({ bestallningar }) => {
  return (
    <div>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Efterfrågat intyg</th>
            <th>Personnummer</th>
            <th>Namn</th>
            <th>Status</th>
            <th>Inkommet datum</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {bestallningar.map(bestallning => (
            <tr key={bestallning.id}>
              <td>{bestallning.id}</td>
              <td>{bestallning.intygName}</td>
              <td>{bestallning.patient.id}</td>
              <td>{bestallning.patient.name}</td>
              <td>{bestallning.status}</td>
              <td>{bestallning.ankomstDatum}</td>
              <td>
                <Link to={`/bestallning/${bestallning.id}`}>Visa</Link>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

BestallningarList.propTypes = {
  bestallningar: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.string.isRequired,
      name: PropTypes.string.isRequired
    })
  )
};

export default BestallningarList;
