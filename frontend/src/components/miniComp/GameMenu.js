import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createEntities } from "../../_api/game.service";
import "./GameMenu.css";



function GameMenu({ onGameStatusChange, users }) {
    const navigate = useNavigate();

    const handleNewGameClick = async () => {
        const { data: token } = await createEntities(users.player1.login, users.player2.login);
        onGameStatusChange(token, "prepare");
    };

    const handleReviewClick = async () => {
        onGameStatusChange(null, "review");
    };

    function handleExitClick() {
        navigate('/');  // Navigate to the home page
    }

    function handleScoreClick() {
        onGameStatusChange(null, "score");
    }


    return (
        <div>
            <div class="menu-container">
                <button class="menu-button play-butt" onClick={handleNewGameClick}></button>
                <button class="menu-button tavern-butt" ></button>
                <button class="menu-button score-butt" onClick={handleScoreClick}></button>
                <button class="menu-button review-butt" onClick={handleReviewClick}></button>
                <button class="menu-button help-butt" ></button>
                <button class="menu-button exit-butt" onClick={handleExitClick}></button>
            </div>
        </div >
    );
}

export default GameMenu;


