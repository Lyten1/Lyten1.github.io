import React from 'react';
import { useState, useEffect } from 'react';
import { fetchActualUnit, fetchEntities, fetchMoves, makeMove, fetchStatus, fetchScore } from "../../_api/game.service";
import "./Board.css";
import Unit from "./Unit.js";
import { addScore } from '../../_api/score.service';


function Board({ users, token, onGameStatusChange, gameReady, teamPreparing }) {

    const [grid, setGrid] = useState(Array(8).fill().map(() => Array(16).fill("")));
    const [potentialSteps, setPotentialSteps] = useState([]);
    const [actualUnit, setActualUnit] = useState(null);
    const [moved, setMoved] = useState(0);
    const cellSize = [79.69, 67.15];





    const cellStyle = {
        padding: 0,
        backgroundColor: 'transparent',
        minWidth: `${cellSize[0]}px`,
        minHeight: `${cellSize[1]}px`,
        width: `${cellSize[0]}px`,
        height: `${cellSize[1]}px`,
        maxWidth: `${cellSize[0]}px`,
        maxHeight: `${cellSize[1]}px`
    };


    useEffect(() => {
        fetchData();
        gameStatus();
    }, [moved]);




    const fetchData = () => {
        fetchEntities(token).then(response => {
            const newGrid = Array(8).fill().map(() => Array(16).fill(""));

            response.data.forEach(entity => {
                if (entity.x >= 0 && entity.x < 16 && entity.y >= 0 && entity.y < 8) {
                    newGrid[entity.y][entity.x] = entity;
                }
            });
            setGrid(newGrid);
        });
        processFirstQueueItem();
    }

    const gameStatus = async () => {
        const response = await fetchStatus(token);
        let status = response.data;
        if (status !== "playing") {
            await new Promise(resolve => setTimeout(resolve, 1000));
            if (status === "light_win") {
                const score = (await fetchScore(token)).data;
                addScore("HMaM", users.player1.login, score);
            }
            else if (status === "dark_win") {
                const score = (await fetchScore(token)).data;
                addScore("HMaM", users.player2.login, score);
            }
            onGameStatusChange(null, status);
        }
    }


    const fetchBoardData = () => {
        fetchEntities(token).then(response => {
            const newGrid = Array(8).fill().map(() => Array(16).fill(""));
            response.data.forEach(entity => {
                if (entity.x >= 0 && entity.x < 16 && entity.y >= 0 && entity.y < 8) {
                    newGrid[entity.y][entity.x] = entity;
                }
            });

            setTimeout(() => {
                setGrid(newGrid);
            }, 0);

        });
    }




    const processFirstQueueItem = async () => {
        try {
            const response = await fetchActualUnit(token);
            let firstUnit = response.data;
            setActualUnit(firstUnit);
            fetchMoves(token).then(response => {
                setPotentialSteps(response.data);
            });


        } catch (error) {
            console.error('Error fetching unit:', error);
        }
    }

    let isHandlingMove = false;
    const [movingUnit, setMovingUnit] = useState(null);  // Tracks the unit currently in motion


    const handleTileClick = async (rowIndex, cellIndex) => {

        if (isHandlingMove) {
            console.log('A move is already being processed. Please wait.');
            return;
        }

        isHandlingMove = true;

        try {

            const updatedUnit = { ...actualUnit }; // Update position locally


            // Calculate the relative distances
            const deltaX = cellIndex - actualUnit.x;
            const deltaY = rowIndex - actualUnit.y;




            await makeMove(token, rowIndex, cellIndex, actualUnit);

            setActualUnit(null);
            setPotentialSteps([]);
            if (Math.abs(deltaX) < actualUnit.stepsDistance + 1 && Math.abs(deltaY) < actualUnit.stepsDistance + 1)
                setMovingUnit({
                    ...actualUnit,
                    deltaX,
                    deltaY
                });

            setTimeout(async () => {
                await fetchBoardData(); // Fetch the entire board state to reflect any other changes
                await processFirstQueueItem();
                setMoved(prev => prev + 1);
                setMovingUnit(null);
            }, 600)
        } catch (error) {
            console.error("Failed to make move or fetch data:", error);

        } finally {
            isHandlingMove = false; // Відновлення стану виконання після завершення
        }

    }










    return (
        <div className='container'>
            <div className='players'>
                <div className='player1'>{users.player1.login}</div>
                <div className='player2'>{users.player2.login}</div>
            </div>
            <div className="board">

                <table className='table'>
                    <tbody>
                        {grid.map((row, rowIndex) => (
                            <tr key={rowIndex}>
                                {row.map((cell, cellIndex) => {
                                    // Check if the current cell is within the steps range
                                    const isInRange = potentialSteps.some(coordinate =>
                                        coordinate.x === cellIndex && coordinate.y === rowIndex
                                    );



                                    let actualCell = false;
                                    if (actualUnit !== null) {
                                        if (actualUnit.y === rowIndex && actualUnit.x === cellIndex) {
                                            actualCell = true;
                                        }
                                    }
                                    let cellClass;
                                    if (isInRange && actualUnit.team === "LIGHT")
                                        cellClass = 'tileToMoveLight';
                                    else if (isInRange && actualUnit.team === "DARK")
                                        cellClass = 'tileToMoveDark';
                                    else if (actualCell)
                                        cellClass = 'actualTile';
                                    else
                                        cellClass = 'without-border';




                                    const onClickHandler = isInRange ? () => handleTileClick(rowIndex, cellIndex) : undefined;
                                    return (
                                        <Unit key={cellIndex} entity={cell} cellIndex={cellIndex} className={cellClass} onClick={onClickHandler}
                                            movingUnit={movingUnit} cellSize={cellSize} cellStyle={cellStyle} />
                                    );
                                })}
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );

}

export default Board;


