import React, { Fragment } from "react";
import { Button } from "reactstrap";
import { DownIcon, UpIcon, UpDownIcon } from "../styles/IbSvgIcons";
import styled from 'styled-components'

// IE11 hack to fix gray background color on server
const SortButton = styled(Button)`
  background-color: transparent !important;
`

const TableSortHead = ({
  currentSortColumn,
  currentSortDirection,
  text,
  sortId,
  onSort
}) => {
  const handleSort = sortColumn => {
    onSort(sortColumn);
  };

  const renderSortIcon = sortColumn => {
    if (currentSortColumn === sortColumn) {
      return currentSortDirection === "DESC" ? <DownIcon /> : <UpIcon />;
    } else {
      return <UpDownIcon />;
    }
  };

  return (
    <Fragment>
      <th>
        <SortButton
          color="link"
          onClick={() => {
            handleSort(sortId);
          }}
        >
          {text}
        </SortButton>{" "}
        {renderSortIcon(sortId)}
      </th>
    </Fragment>
  );
};

export default TableSortHead;
