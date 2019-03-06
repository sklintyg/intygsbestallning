import React from 'react';
import { Link } from "react-router-dom";

const BestallningarTable = () => {
    const bestallningar = [
        {
            name: 'Test',
            id: '123'
        }
    ];

    return (
      <div>
        <table>
          <tbody>
            {bestallningar.map((bestallning) => 
              <tr>
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
};

export default BestallningarTable;
