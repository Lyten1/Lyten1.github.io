import React from 'react';
import './App.css'; // Assume your CSS is in this file

// Sprite component
const Sprite = ({ x, y, width, height }) => {
    const style = {
        display: 'inline-block',
        backgroundImage: 'url("/path-to-your-sprite-sheet.png")',
        backgroundPosition: `-${x}px -${y}px`,
        width: `${width}px`,
        height: `${height}px`
    };

    return <div style={style} />;
};

// App component
const App = () => {
    return (
        <div>
            {/* Example of using the Sprite component for one sprite */}
            <Sprite x={0} y={0} width={64} height={64} />
            {/* You would replace x, y, width, and height with the actual values for your sprite */}
        </div>
    );
};

export default App;
