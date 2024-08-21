import React, { useRef, useEffect, useState } from 'react';
import Score from './miniComp/Score';
import { addScore, fetchScore } from '../_api/score.service';


function ScoreService({ status, users }) {

    const [score, setScore] = useState([]);



    const fetchData = () => {
        fetchScore("HMaM").then(response => {
            setScore(response.data);
        });

    };

    useEffect(() => {
        fetchData();
    });






    const pageStyles = {
        backgroundColor: 'rgba(193, 210, 192, 0.90)',
        width: '90%',
        display: 'flex',
        flexDirection: 'column',
        margin: 'auto',
        textAlign: 'center',
        padding: '30px 0px',
        borderRadius: '15px',
    };


    const scoreSt = {
        width: '80%',
        margin: 'auto',
        marginTop: '40px',
        marginBottom: '40px',
    }

    return (
        <div style={pageStyles}>
            {status === "light_win" && users?.player1 &&
                (<h1>Congratulations {users.player1.login}</h1>)
            }
            {status === "dark_win" && users?.player2 &&
                (<h1>Congratulations {users.player2.login}</h1>)
            }
            {status === "tie" &&
                (<h1>Good job, Heroes, tie</h1>)
            }
            <div style={scoreSt}>
                <h1>Score</h1 >
                <Score scores={score} />
            </div>
        </div>
    );

}



export default ScoreService;