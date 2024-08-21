import React, { useEffect, useState } from 'react';
import ReactDOM from 'react-dom';
import './App.css';
import { BrowserRouter, Route, Routes } from "react-router-dom";
import BackgroundImageComponent from './components/miniComp/BackgroundImage';
import Login_Register from './components/LoginRegister'; // Uncomment if needed
import GameService from './components/GameService';
import { fetchEntities } from "./_api/game.service";

import Unit from "./components/miniComp/Unit.js";

import { DndProvider } from 'react-dnd';
import { HTML5Backend } from 'react-dnd-html5-backend';
import DraggableBox from './components/miniComp/DraggableBox';
import DragZone from './components/miniComp/DragZone';


function App() {

  const [boxes, setBoxes] = useState([
    { id: 1, text: 'Box 1', isActive: true },
    { id: 2, text: 'Box 2', isActive: true },
    { id: 3, text: 'Box 3', isActive: true },
    { id: 4, text: 'Box 4', isActive: true },
    { id: 5, text: 'Box 5', isActive: true },
    { id: 6, text: 'Box 6', isActive: true },
    { id: 7, text: 'Box 7', isActive: true },
  ]);

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

  const [zones, setZones] = useState(initialZones);

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



  return (
    <div>
      <BackgroundImageComponent />

      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Login_Register />} />
          <Route path="/game" element={<GameService />} />
        </Routes>
      </BrowserRouter>



    </div>
  );
}

export default App;
