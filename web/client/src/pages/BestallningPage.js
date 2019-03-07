import React from 'react';
import {Link} from 'react-router-dom'
import './BestallningPage.css'

const BestallningPage = ({match}) => {
    return (
        
        <div className='colorize'>
            <div><Link to="/bestallningar">Tillbaka</Link></div>
            Beställning {match.params.id}
        </div>
    )
};

export default BestallningPage;
