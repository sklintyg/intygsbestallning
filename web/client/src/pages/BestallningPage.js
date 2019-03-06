import React from 'react';
import {Link} from 'react-router-dom'

const BestallningPage = ({match}) => {
    return (
        
        <div>
            <div><Link to="/bestallningar">Tillbaka</Link></div>
            Beställning {match.params.id}
        </div>
    )
};

export default BestallningPage;
