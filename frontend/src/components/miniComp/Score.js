import Table from 'react-bootstrap/Table';


function Score({ scores }) {
    return (
        <div>
            <Table striped bordered hover>
                <thead>
                    <tr>
                        <th>Player</th>
                        <th>Score</th>
                        <th>Date</th>
                    </tr>
                </thead>
                <tbody>
                    {scores.map(score => (
                        <tr key={"comment-" + score.ident}>
                            <td>{score.player}</td>
                            <td>{score.points}</td>
                            <td>{new Date(score.playedOn).toLocaleString()}</td>
                        </tr>
                    ))}
                </tbody>
            </Table>
        </div>
    );
}

export default Score;

