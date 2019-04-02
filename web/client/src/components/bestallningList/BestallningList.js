import React from "react";
import { Link } from "react-router-dom";
import styled from "styled-components";
import TableSortHead from "./TableSortHead";
import { Error } from '../styles/IbSvgIcons'
import {Table, Button} from 'reactstrap'

const ResultLine = styled.div`
  padding: 20px 0 10px 0;
`;

const BestallningarList = ({ bestallningList, onSort }) => {
  if (bestallningList.bestallningar.length === 0) {
    return (
      <ResultLine>
        Inget resultat hittades för den valda filtreringen. Överväg att ändra
        filtreringen för att utöka resultatet.
      </ResultLine>
    );
  }

  const handleSort = sortColumn => {
    onSort(sortColumn);
  };

  return (
    <div>
      <ResultLine>
        Visar {bestallningList.start}-{bestallningList.end} av{" "}
        {bestallningList.totalElements} träffar
      </ResultLine>
      <Table striped className="ib-table-striped">
        <thead>
          <tr>
            <TableSortHead
              currentSortColumn={bestallningList.sortColumn}
              currentSortDirection={bestallningList.sortDirection}
              text="ID"
              sortId="ID"
              onSort={handleSort}
            />
            <TableSortHead
              currentSortColumn={bestallningList.sortColumn}
              currentSortDirection={bestallningList.sortDirection}
              text="Efterfrågat intyg"
              sortId="INTYG_TYP"
              onSort={handleSort}
            />
            <TableSortHead
              currentSortColumn={bestallningList.sortColumn}
              currentSortDirection={bestallningList.sortDirection}
              text="Personnummer"
              sortId="INVANARE_PERSON_ID"
              onSort={handleSort}
            />
            <TableSortHead
              currentSortColumn={bestallningList.sortColumn}
              currentSortDirection={bestallningList.sortDirection}
              text="Status"
              sortId="STATUS"
              onSort={handleSort}
            />
            <TableSortHead
              currentSortColumn={bestallningList.sortColumn}
              currentSortDirection={bestallningList.sortDirection}
              text="Inkommet datum"
              sortId="ANKOMST_DATUM"
              onSort={handleSort}
            />
            <th />
          </tr>
        </thead>
        <tbody>
          {bestallningList.bestallningar.map(bestallning => (
            <tr key={bestallning.id}>
              <td>{bestallning.id}</td>
              <td>{bestallning.intygTyp}</td>
              <td>{bestallning.invanare.personId}</td>
              <td>{bestallning.status === 'Oläst' ? <Error /> : null } {bestallning.status}</td>
              <td>{bestallning.ankomstDatum}</td>
              <td>
                <Link to={`/bestallning/${bestallning.id}`}><Button color="primary">Visa</Button></Link>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  );
};

export default BestallningarList;
