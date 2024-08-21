import React, { useEffect, useState, useRef } from 'react';
import Board from './miniComp/Board';
import GameMenu from './miniComp/GameMenu';
import { useLocation } from 'react-router-dom';
import FeedbackService from './FeedbackService';
import ScoreService from './ScoreService';
import backgroundMusic from './music/main.mp3';
import battleMusic from './music/battle.mp3';

import { DndProvider } from 'react-dnd';
import { HTML5Backend } from 'react-dnd-html5-backend';
import DraggableBox from './miniComp/DraggableBox';
import DragZone from './miniComp/DragZone';
import { fetchPlayersUnits, addUnits } from '../_api/game.service';

function GameService() {
    const location = useLocation();
    const users = location.state?.users;

    const [token, setToken] = useState(() => localStorage.getItem('token'));
    const [gameStatus, setGameStatus] = useState(() => localStorage.getItem('gameStatus') || 'menu');
    const [isAudioPlaying, setIsAudioPlaying] = useState(false);
    const [actualMusic, setActualMusic] = useState("std");
    const audioRef = useRef(new Audio());
    const [teamPreparing, setTeamPreparing] = useState(() => localStorage.getItem('teamPreparing') || 'light');
    const [mapName, setMapName] = useState("grove");

    const [zones, setZones] = useState('auto');
    const [unitsData, setUnitsData] = useState("");

    const initialZones = {
        zone1: null,
        zone2: null,
        zone3: null,
        zone4: null,
        zone5: null,
        zone6: null,
        zone7: null,
        zone8: null,
        zone9: null,
        zone10: null,
        zone11: null,
        zone12: null,
        zone13: null,
        zone14: null,
        zone15: null,
        zone16: null,
    };

    useEffect(() => {
        audioRef.current.loop = true;
        const playAudio = async () => {
            try {
                await audioRef.current.play();
                setIsAudioPlaying(true);
            } catch (err) {
                console.error('Error playing audio:', err);
                setIsAudioPlaying(false);
            }
        };
        audioRef.current.src = backgroundMusic;
        playAudio();
    }, []);

    useEffect(() => {
        localStorage.setItem('token', token);
        localStorage.setItem('gameStatus', gameStatus);
    }, [token, gameStatus]);


    const readBoxesData = async () => {
        let name = users.player1.login;
        if (teamPreparing === "dark") {
            name = users.player2.login;
        }

        try {
            setUnitsData(await fetchPlayersUnits(token, name).data);
            setBoxesAndZones(await fetchPlayersUnits(token, name).data);
        } catch (error) {
            console.error('Error fetching player units:', error);
        }
    };



    const setBoxesAndZones = (unitsData) => {
        if (unitsData !== "") {
            const unitsCountArray = unitsData.split("-");
            const unitsMap = unitsCountArray.reduce((acc, unit) => {
                const [name, count] = unit.split(":");
                acc[name] = Number(count);
                return acc;
            }, {});

            console.log(unitsMap);

            const boxes = [
                { id: 1, text: 'Pikeman', isActive: unitsMap['Pikeman'] > 0 },
                { id: 2, text: 'Archer', isActive: unitsMap['Archer'] > 0 },
                { id: 3, text: 'Griffin', isActive: unitsMap['Griffin'] > 0 },
                { id: 4, text: 'Monk', isActive: unitsMap['Monk'] > 0 },
                { id: 5, text: 'Swordsman', isActive: unitsMap['Swordsman'] > 0 },
                { id: 6, text: 'Cavalier', isActive: unitsMap['Cavalier'] > 0 },
                { id: 7, text: 'Angel', isActive: unitsMap['Angel'] > 0 },
            ];

            setBoxes(boxes);
            setZones(initialZones);
        }
    }


    // useEffect(() => {
    //     setBoxesAndZones();
    // });

    const handleGameStatusChange = (newToken, status) => {
        readBoxesData();
        setBoxesAndZones();
        setToken(newToken);
        setGameStatus(status);

        localStorage.setItem('teamPreparing', "light");
    };

    useEffect(() => {
        const newSrc = gameStatus === "playing" ? battleMusic : backgroundMusic;
        const newMusic = gameStatus === "playing" ? 'battle' : 'std';
        if (actualMusic !== newMusic) {
            audioRef.current.pause();
            audioRef.current.src = newSrc;
            setActualMusic(newMusic);
            audioRef.current.play().catch(err => console.error('Error playing audio:', err));
        }
    }, [gameStatus, actualMusic]);



    const buttonStyle = {
        backgroundColor: '#DFD0B8',
        color: 'white',
        padding: '10px 20px',
        fontSize: '16px',
        border: 'none',
        borderRadius: '5px',
        cursor: 'pointer',
        outline: 'none',
        boxShadow: '0 4px #948979',
        transition: 'all 0.3s',
        margin: '5px'
    }

    const startAudioManually = () => {
        if (!isAudioPlaying) {
            audioRef.current.play().then(() => {
                setIsAudioPlaying(true);
            }).catch(err => {
                console.error('Manual play error:', err);
            });
        }
    };

    const [boxes, setBoxes] = useState([]);





    const handleDrop = (itemId, targetZoneId) => {
        if (targetZoneId) {
            setZones((prevZones) => {
                const previousZoneId = Object.keys(prevZones).find(zoneId => prevZones[zoneId] === itemId);
                return {
                    ...prevZones,
                    [previousZoneId]: null,
                    [targetZoneId]: itemId,
                };
            });
            setBoxes((prevBoxes) =>
                prevBoxes.map((box) =>
                    box.id === itemId ? { ...box, isActive: false } : box
                )
            );
        }
    };

    const fieldStyle = {
        backgroundImage: `url("/images/${mapName}.png")`,
        /* snow,
        swamp,
        fallout,
        grove */

        backgroundRepeat: 'no-repeat',
        backgroundSize: 'cover',
        border: '10px solid rgb(146, 175, 166)',
        minWidth: '1320px',
        width: '1320px',
        maxWidth: '1320px',
        margin: 'auto',
        paddingTop: '50px',
        color: '#a9aaab'
    };

    const handleApprove = () => {
        if (teamPreparing === "light") {
            //fetch data to server
            const result = prepareJsonData();
            // console.log(result);
            addUnits(token, result);
            setTeamPreparing("dark");
            readBoxesData();
        } else if (teamPreparing === "dark") {
            //fetch data to server
            const result = prepareJsonData();
            setTeamPreparing("end");
            setGameStatus("playing");
        }
    };

    const prepareJsonData = () => {
        const jsonData = Object.keys(zones).reduce((result, zoneId) => {
            if (zones[zoneId] !== null) {
                const box = boxes[zones[zoneId] - 1];
                const zoneNumber = parseInt(zoneId.replace('zone', ''), 10);
                result.push({
                    name: box.text,
                    team: teamPreparing,
                    x: Math.floor((zoneNumber - 1) / 2),
                    y: (zoneNumber % 2) === 1 ? 0 : 1,
                });
            }
            return result;
        }, []);

        return jsonData;
    };

    return (
        <div>
            {!isAudioPlaying && (
                <button onClick={startAudioManually}>
                    🔊
                </button>
            )}
            {gameStatus === "menu" && <GameMenu onGameStatusChange={handleGameStatusChange} users={users} />}
            {(gameStatus === "score" || gameStatus === "light_win" || gameStatus === "dark_win" || gameStatus === "tie") &&
                <div>
                    <div style={{
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                        height: '70px',
                    }}>
                        <button style={buttonStyle} onMouseOver={({ target }) => target.style.backgroundColor = '#B2A693'}
                            onMouseOut={({ target }) => target.style.backgroundColor = '#DFD0B8'}
                            onClick={() => setGameStatus("menu")}>
                            Return to Menu
                        </button>
                    </div>
                    <ScoreService status={gameStatus} users={users} />
                </div>
            }
            {gameStatus === "review" &&
                <div>
                    <div style={{
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                        height: '70px',
                    }}><button style={buttonStyle} onMouseOver={({ target }) => target.style.backgroundColor = '#B2A693'}
                        onMouseOut={({ target }) => target.style.backgroundColor = '#DFD0B8'}
                        onClick={() => setGameStatus("menu")}>
                            Return to Menu</button></div>
                    <FeedbackService />
                </div>
            }
            {gameStatus === "prepare" && (
                <div style={{
                    display: 'flex',
                    flexDirection: 'column',
                }}>
                    <div style={{
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                    }}>
                        <button style={buttonStyle} onMouseOver={({ target }) => target.style.backgroundColor = '#B2A693'}
                            onMouseOut={({ target }) => target.style.backgroundColor = '#DFD0B8'}
                            onClick={() => setGameStatus("menu")}>
                            Return to Menu</button>
                    </div>
                    {teamPreparing !== "end" &&
                        <DndProvider backend={HTML5Backend}>

                            <div style={{ display: 'flex', justifyContent: 'center', marginTop: '20px' }}>
                                {boxes.map((box) => (
                                    <DraggableBox
                                        key={box.id}
                                        id={box.id}
                                        text={box.text}
                                        isActive={box.isActive}
                                        team={teamPreparing}
                                        styleMargin={{ margin: '10px' }}
                                    />
                                ))}
                            </div>

                            <div style={fieldStyle}>
                                {teamPreparing === "light" && (
                                    <div>
                                        <button onClick={handleApprove} style={{ marginLeft: "20px", width: '150px', height: '50px', borderRadius: "20px", fontSize: "20pt" }}>Approve</button>
                                        <div style={{ display: 'flex', justifyContent: 'left', marginTop: '4px' }}>
                                            <div style={{ display: 'flex', flexWrap: 'wrap', width: '170px', margin: '10px' }}>
                                                {Object.keys(initialZones).map((zoneId) => (
                                                    <DragZone key={zoneId} id={zoneId} onDrop={handleDrop}>
                                                        {zones[zoneId] && (
                                                            <DraggableBox
                                                                id={zones[zoneId]}
                                                                text={boxes[zones[zoneId] - 1].text}
                                                                isActive={true}
                                                                team={teamPreparing}
                                                            />
                                                        )}
                                                    </DragZone>
                                                ))}
                                            </div>
                                        </div>
                                    </div>
                                )}
                                {teamPreparing === "dark" &&
                                    <div >
                                        <div style={{ display: 'flex', justifyContent: 'right' }}>
                                            <button onClick={handleApprove} style={{ marginRight: '20px', width: '150px', height: '50px', borderRadius: "20px", fontSize: "20pt" }}>Approve</button>
                                        </div>
                                        <div style={{ display: 'flex', justifyContent: 'right', marginTop: '4px' }}>
                                            <div style={{ display: 'flex', flexWrap: 'wrap', width: '170px', margin: '10px' }}>
                                                {Object.keys(initialZones).map((zoneId) => (
                                                    <DragZone key={zoneId} id={zoneId} onDrop={handleDrop}>
                                                        {zones[zoneId] && (
                                                            <DraggableBox
                                                                id={zones[zoneId]}
                                                                text={boxes[zones[zoneId] - 1].text}
                                                                isActive={true}
                                                                team={teamPreparing}
                                                            />
                                                        )}
                                                    </DragZone>
                                                ))}
                                            </div>
                                        </div>
                                    </div>
                                }
                            </div>
                        </DndProvider>
                    }
                </div >
            )}
            {gameStatus === "playing" && (
                <div style={{
                    display: 'flex',
                    flexDirection: 'column',
                }}>
                    <div style={{
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                    }}>
                        <button style={buttonStyle} onMouseOver={({ target }) => target.style.backgroundColor = '#B2A693'}
                            onMouseOut={({ target }) => target.style.backgroundColor = '#DFD0B8'}
                            onClick={() => setGameStatus("menu")}>
                            Return to Menu</button>
                    </div>
                    <Board users={users} token={token} onGameStatusChange={handleGameStatusChange} gameReady={gameStatus} battleMusic={battleMusic} backgroundMusic={backgroundMusic} />
                </div>
            )}
        </div >
    );
}

export default GameService;
