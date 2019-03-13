import React from "react";
import { Link } from "react-router-dom";
import styled from 'styled-components'

const Table = styled.table`

  padding-top: 10px;

  & th {
    text-align: left;
  }

  & td {
    background-color: #eee;
    padding: 5px;
  }
`;

const BestallningarList = ({ bestallningar }) => {
  return (
    <div>
      <Table>
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
      </Table>
    </div>
  );
};

export default BestallningarList;
