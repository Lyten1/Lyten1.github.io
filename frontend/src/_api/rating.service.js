import gsAxios from ".";
import { formatDate } from "./utils";

export const fetchAvgRating = function (game) {
    return gsAxios.get('/rating/' + game);
}



export const addRating = function (game, player, rating) {
    return gsAxios.post('/rating', {
        game: game,
        player: player,
        rating: rating,
        ratedOn: formatDate(new Date()),
    });

}
