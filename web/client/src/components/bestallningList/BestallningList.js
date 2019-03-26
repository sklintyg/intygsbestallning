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

const ResultLine = styled.div`
  padding-top: 20px;
`;

const BestallningarList = ({ bestallningList }) => {

  if(bestallningList.bestallningar.length === 0){
    return (
      <ResultLine>Inget resultat hittades för den valda filtreringen. Överväg att ändra filtreringen för att utöka resultatet.</ResultLine>
    )
  }

  return (
    <div>
      <ResultLine>Sökresultat: {bestallningList.bestallningar.length} av {bestallningList.totalCount} beställningar</ResultLine>
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
          {bestallningList.bestallningar.map(bestallning => (
            <tr key={bestallning.id}>
              <td>{bestallning.id}</td>
              <td>{bestallning.intygTyp}</td>
              <td>{bestallning.invanare.personId}</td>
              <td>{bestallning.invanare.name}</td>
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
