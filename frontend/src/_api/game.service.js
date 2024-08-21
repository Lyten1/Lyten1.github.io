import gsAxios from ".";

export const createEntities = function (login1, login2) {
    return gsAxios.post(`/createGame?login1=${login1}&login2=${login2}`);
}


export const fetchEntities = function (token) {
    return gsAxios.get(`/entities?token=${token}`);
}


export const fetchActualUnit = function (token) {
    return gsAxios.get(`/getActualUnit?token=${token}`);
}


export const fetchMoves = function (token) {
    return gsAxios.get(`/potentialMoves?token=${token}`);
}


export const makeMove = function (token, newY, newX, unit) {
    const jsonText = JSON.stringify(unit);
    const encodedText = encodeURIComponent(jsonText);
    return gsAxios.post(`/move?token=${token}&newY=${newY}&newX=${newX}&text=${encodedText}`);
}

export const fetchStatus = function (token) {
    return gsAxios.get(`/gameStatus?token=${token}`);
}

export const fetchScore = function (token) {
    return gsAxios.get(`/getWinCost?token=${token}`);
}

export const fetchPlayersUnits = function (token, player) {
    return gsAxios.get(`/getPlayersUnits?token=${token}&player=${player}`);
}

export const addUnits = function (token, data) {
    return gsAxios.post(`/addUnits?token=${token}`, data, {
        headers: {
            'Content-Type': 'application/json'
        }
    });
}