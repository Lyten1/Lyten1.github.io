import React, { useState, useEffect } from 'react';

function Rating({ rating }) {
    const divStyles = {
        marginBottom: '30px',
    };

    // Define the keyframes for the animation using a unique class
    const keyframesStyle = `
        @keyframes pulse {
            0% { color: black; }
            50% { color: grey; }
            100% { color: black; }
        }
    `;

    // Add the animation CSS directly into the component
    const [styleTag, setStyleTag] = useState('');

    useEffect(() => {
        const styleEl = document.createElement('style');
        document.head.appendChild(styleEl);
        styleEl.textContent = keyframesStyle;
        setStyleTag(styleEl);

        return () => {
            document.head.removeChild(styleEl);
        };
    }, []);

    // Inline styles for the animated element
    const animatedTextStyle = {
        animation: 'pulse 2s infinite',
    };

    return (
        <div style={divStyles}>
            <h3 style={animatedTextStyle}>Average rating: {rating}</h3>
        </div>
    );
}

export default Rating;
