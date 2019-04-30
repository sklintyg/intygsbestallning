import React from 'react'
import { Link } from 'react-router-dom'
import styled from 'styled-components'
import TableSortHead from './TableSortHead'
import { Error } from '../styles/IbSvgIcons'
import { Table, Button } from 'reactstrap'
import FetchError from './FetchError'
import { getMessage } from '../../messages/messages'

const ResultLine = styled.div`
  padding: 20px 0 10px 0;
`

const Wrapper = styled.div`
  & th:last-child {
    width: 1%;
  }
`

const BestallningarList = ({ bestallningList, onSort, errorMessage, categoryFilter }) => {
  if (bestallningList.bestallningar && bestallningList.bestallningar.length === 0) {
    if (bestallningList.ingaRegistreradeBestallningar) {
      return (
        <ResultLine>
          Det finns inga {getMessage('list.category.' + categoryFilter)} beställningar eller förfrågningar för den enhet som du inloggad på
        </ResultLine>
      )
    } else {
      return <ResultLine>Inga träffar för den valda filtreringen. Överväg att ändra filtreringen för att utöka resultatet.</ResultLine>
    }
  }

  const handleSort = (sortColumn) => {
    onSort(sortColumn)
  }

  return (
    <Wrapper>
      <ResultLine>
        Visar {bestallningList.start}-{bestallningList.end} av {bestallningList.totalElements} träffar
      </ResultLine>
      <Table striped>
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
        <tbody id={'BestallningListTable'}>
          {errorMessage && (
            <tr>
              <td colSpan={5}>
                <FetchError message={errorMessage} />
              </td>
            </tr>
          )}
          {!errorMessage &&
            bestallningList.bestallningar &&
            bestallningList.bestallningar.map((bestallning) => (
              <tr key={bestallning.id}>
                <td>{bestallning.id}</td>
                <td>{bestallning.intygTyp}</td>
                <td>{bestallning.invanare.personId}</td>
                <td>
                  {bestallning.status === 'Oläst' ? <Error /> : null} {bestallning.status}
                </td>
                <td>{bestallning.ankomstDatum}</td>
                <td>
                  <Link
                    to={{
                      pathname: '/bestallning/' + bestallning.id,
                      search: '',
                      hash: '',
                      state: { fromList: categoryFilter },
                    }}
                    id={'BestallningListButton-' + bestallning.id}>
                    <Button color="primary">Visa</Button>
                  </Link>
                </td>
              </tr>
            ))}
        </tbody>
      </Table>
    </Wrapper>
  )
}

export default BestallningarList
