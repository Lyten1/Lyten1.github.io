import { useState, useEffect } from 'react';
import "./Board.css";

const Unit = ({ entity, className, onClick, movingUnit, cellSize, cellStyle }) => {
    const [backgroundSize, setBackgroundSize] = useState('auto');
    const [wayOfSee, setWayOfSee] = useState('auto');
    const [imgSrc, setSrc] = useState('auto');
    const [currentFrame, setCurrentFrame] = useState(0);
    const [direction, setDirection] = useState(1); // 1 for forward, -1 for backward
    const scale = 0.38;

    const name = entity.name;



    let spriteWidth = 0;
    let spriteHeight = 0;
    let frameCount = 0;
    let frameRate = 0;

    let x = 0;
    let y = 0;

    let slidewidth = 0;

    switch (entity.name) {
        case "Pikeman":
            spriteWidth = 60; spriteHeight = 100; x = 0; y = 6;
            frameCount = 8; frameRate = 150; slidewidth = 61;
            break;
        case "Archer":
            spriteWidth = 70; spriteHeight = 90; x = 320 * scale; y = 0;
            frameCount = 6; frameRate = 150; slidewidth = 71;
            break;
        case "Angel":
            spriteWidth = 100; spriteHeight = 115; x = 0; y = 0;
            frameCount = 5; frameRate = 550; slidewidth = 151;
            break;
        case "Cavalier":
            spriteWidth = 115; spriteHeight = 120; x = 0; y = 0;
            frameCount = 5; frameRate = 250; slidewidth = 115;
            break;
        case "Griffin":
            spriteWidth = 95; spriteHeight = 120; x = 62 * scale; y = 3;
            frameCount = 6; frameRate = 250; slidewidth = 95;
            break;
        case "Monk":
            spriteWidth = 70; spriteHeight = 90; x = 0; y = 0;
            frameCount = 7; frameRate = 250; slidewidth = 82;
            break;
        case "Swordsman":
            spriteWidth = 55; spriteHeight = 90; x = 1; y = 10;
            frameCount = 9; frameRate = 250; slidewidth = 49;
            break;
        default:
            break;
    }

    if (entity.state === 'BLOCKED') {
        spriteWidth = 95; spriteHeight = 95; x = 0; y = 0;
        frameCount = 1; frameRate = 100;
    }

    useEffect(() => {
        const img = new Image();

        if (entity.state === 'BLOCKED') {
            img.src = `./images/rock-long.png`;
        } else {
            img.src = `./images/units/${name}/all.png`;
        }
        setSrc(img.src);
        img.onload = () => {
            setBackgroundSize(`${img.naturalWidth * scale}px ${img.naturalHeight * scale}px`);
            if (entity.state === 'BLOCKED') {
                setWayOfSee(`scaleX(1)`);
            } else {
                setWayOfSee(entity.team === "DARK" ? `scaleX(-1)` : `scaleX(1)`);
            }
        };
    }, [entity, name]);

    useEffect(() => {
        if (frameCount > 1) {
            const interval = setInterval(() => {
                setCurrentFrame((prevFrame) => {
                    if (prevFrame === frameCount - 1 && direction === 1) {
                        setDirection(-1);
                        return prevFrame - 1;
                    } else if (prevFrame === 0 && direction === -1) {
                        setDirection(1);
                        return prevFrame + 1;
                    } else {
                        return prevFrame + direction;
                    }
                });
            }, frameRate);
            return () => clearInterval(interval);
        }
    }, [frameRate, frameCount, direction]);




    const isMoving = movingUnit && movingUnit.x === entity.x && movingUnit.y === entity.y;


    const style = isMoving ? {
        transform: `translate(${movingUnit.deltaX * cellSize[0]}px, ${movingUnit.deltaY * (cellSize[1])}px)`,
        transition: 'transform 0.6s ease',
        padding: '0px',
    } : {
        padding: '0px',
    };


    const frameX = x + (slidewidth * currentFrame * scale);

    const sprBack = {
        margin: 'auto',
        backgroundImage: `url(${imgSrc})`,
        backgroundPosition: `-${frameX}px -${y}px`,
        width: `${spriteWidth * scale}px`,
        height: `${spriteHeight * scale}px`,
        backgroundSize: backgroundSize,
        transform: wayOfSee,
        transition: 'none'
    };

    const textStyle = {
        textAlign: 'right',
        backgroundColor: 'rgb(128, 176, 240)',
        borderRadius: '2px',
        border: '2px solid rgb(54, 132, 234)',
        padding: `${2 * scale}px ${3 * scale}px`,
        fontSize: `${19 * scale}pt`
    };

    const containerStyle = {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'flex-end',
        width: '95%'
    };

    return (
        <td style={{ ...style, ...cellStyle }} className={className} onClick={onClick}>
            {(entity.state === "BLOCKED" || entity.amount > 0) && (
                <div style={containerStyle}>
                    <div style={sprBack}></div>
                    {(entity.state !== "BLOCKED" && <div style={textStyle}>{entity.amount}</div>)}
                </div>
            )}
        </td>
    );
};

export default Unit;
