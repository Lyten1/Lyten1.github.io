// DraggableBox.js
import React, { useState, useEffect } from 'react';
import { useDrag } from 'react-dnd';


const DraggableBox = ({ id, isActive, text, onDropped, team, styleMargin }) => {
    const [{ isDragging }, drag, preview] = useDrag(() => ({
        type: 'BOX',
        item: { id },
        canDrag: isActive,
        end: (item, monitor) => {
            if (monitor.didDrop() && onDropped) {
                onDropped(item.id);
            }
        },
        collect: (monitor) => ({
            isDragging: !!monitor.isDragging(),
        }),
    }));

    const scale = 0.38;

    let spriteWidth = 0;
    let spriteHeight = 0;

    let x = 0;
    let y = 0;



    switch (text) {
        case "Pikeman":
            spriteWidth = 60; spriteHeight = 100; x = 0; y = 6;
            break;
        case "Archer":
            spriteWidth = 70; spriteHeight = 90; x = 320 * scale; y = 0;
            break;
        case "Angel":
            spriteWidth = 100; spriteHeight = 115; x = 0; y = 0;
            break;
        case "Cavalier":
            spriteWidth = 115; spriteHeight = 120; x = 0; y = 0;
            break;
        case "Griffin":
            spriteWidth = 95; spriteHeight = 120; x = 62 * scale; y = 3;
            break;
        case "Monk":
            spriteWidth = 70; spriteHeight = 90; x = 0; y = 0;
            break;
        case "Swordsman":
            spriteWidth = 55; spriteHeight = 90; x = 1; y = 10;
            break;
        default:
            break;
    }

    const [backgroundSize, setBackgroundSize] = useState('auto');
    const [wayOfSee, setWayOfSee] = useState('auto');
    const [imgSrc, setSrc] = useState('auto');

    useEffect(() => {
        const img = new Image();
        img.src = `./images/units/${text}/all.png`;

        setSrc(img.src);

        img.onload = () => {
            setBackgroundSize(`${img.naturalWidth * scale}px ${img.naturalHeight * scale}px`);
            setWayOfSee(team === "dark" ? `scaleX(-1)` : `scaleX(1)`);

        };
    }, [text, team]);

    const sprBack = {
        margin: 'auto',
        backgroundImage: `url(${imgSrc})`,
        backgroundPosition: `-${x}px -${y}px`,
        width: `${spriteWidth * scale}px`,
        height: `${spriteHeight * scale}px`,
        backgroundSize: backgroundSize,
        transform: wayOfSee,
        transition: 'none'
    };


    return (
        <div
            ref={isActive ? drag : preview}
            style={{
                opacity: isDragging ? 0.5 : 1,
                backgroundColor: isActive ? 'lightblue' : 'gray',
                width: '79.69px',
                height: '67.15px',
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                border: '1px solid black',
                cursor: isActive ? 'move' : 'not-allowed',
                ...styleMargin
            }}
        >
            <div style={sprBack}></div>
        </div>
    );
};

export default DraggableBox;
