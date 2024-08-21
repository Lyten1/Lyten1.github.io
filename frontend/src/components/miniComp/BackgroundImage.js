import React, { useEffect } from 'react';

function BackgroundImage() {
    useEffect(() => {
        document.getElementById('root').classList.add('special-background');

        // Повернення до початкового стану при демонтажі компонента
        return () => {
            document.body.classList.remove('special-background');
        };
    }, []);

    return (
        <div>

        </div>
    );
}

export default BackgroundImage;
