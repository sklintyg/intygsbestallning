import React from 'react';

const BestallningPage = ({match, history}) => {
    console.log(history);
    return (
        <div>
            <div onClick={ history.goBack }>Tillbaka</div>
            Beställning {match.params.id}
        </div>
    )
};

export default BestallningPage;
