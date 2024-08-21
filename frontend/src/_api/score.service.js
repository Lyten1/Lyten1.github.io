import gsAxios from ".";
import { formatDate } from "./utils";

export const fetchScore = function (game) {
    return gsAxios.get('/score/' + game);
}



export const addScore = function (game, player, points) {
    return gsAxios.post('/score', {
        game: game,
        player: player,
        points: points,
        playedOn: formatDate(new Date()),
    });

}
