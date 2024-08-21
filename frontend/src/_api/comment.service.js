import gsAxios from ".";
import { formatDate } from "./utils";

export const fetchComments = function (game) {
    return gsAxios.get('/comment/' + game);
}

//const fetchComments = game => gsAxios.get('/comment' + game);

export const addComment = function (game, player, comment) {
    return gsAxios.post('/comment', {
        game: game,
        player: player,
        comment: comment,
        commentedOn: formatDate(new Date()),
    });

}
