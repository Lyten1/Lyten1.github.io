import React from 'react';
import { useDrop } from 'react-dnd';

const DragZone = ({ id, children, onDrop }) => {
    const [{ isOver }, drop] = useDrop(() => ({
        accept: 'BOX',
        drop: (item) => onDrop(item.id, id),
        collect: (monitor) => ({
            isOver: !!monitor.isOver(),
        }),
    }));

    return (
        <div
            ref={drop}
            style={{
                backgroundColor: isOver ? 'lightgreen' : 'lightgrey',
                width: '79.69px',
                height: '67.15px',
                margin: '2px',
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                border: '1px dashed black',
            }}
        >
            {children}
        </div>
    );
};

export default DragZone;
