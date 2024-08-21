import { Form } from 'react-bootstrap';
import { useForm } from "react-hook-form";
import React, { useState } from 'react';

function RatingForm({ combined, onRatingChange, onPlayerChange }) {
    const {
        register,
        watch,
        formState: { errors }
    } = useForm({ mode: "onChange" });

    const player = watch("player");
    const [rating, setRating] = useState(0); // Активний рейтинг
    const [hover, setHover] = useState(0); // Рейтинг, що відображається при наведенні

    React.useEffect(() => {
        if (onPlayerChange) {
            onPlayerChange(player); // Corrected from onCommentChange to onPlayerChange
        }
    }, [player, onPlayerChange]);

    React.useEffect(() => {
        onRatingChange(rating);
    }, [rating]);

    return (
        <Form>
            {!combined && ( // Only render this part if 'combined' is false
                <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                    <Form.Label>Player Name</Form.Label>
                    <Form.Control type="text" placeholder="nickname" {...register("player", { required: "Player name is required" })} />
                    {errors.player && <p style={{ color: 'red' }}>{errors.player.message}</p>}
                </Form.Group>
            )}
            <Form.Group className="mb-3" controlId="exampleForm.ControlTextarea1">
                <Form.Label>Rating</Form.Label>
                <div className="star-rating">
                    {[...Array(5)].map((star, index) => {
                        const ratingValue = index + 1;
                        return (
                            <label key={index}>
                                <input
                                    type="radio"
                                    name="rating"
                                    value={ratingValue}
                                    onClick={() => setRating(ratingValue)}
                                    style={{ display: "none" }} />
                                <i
                                    className="star"
                                    style={{ fontSize: '33px', color: ratingValue <= (hover || rating) ? "#ffc107" : "#a9aaab" }}
                                    onMouseEnter={() => setHover(ratingValue)}
                                    onMouseLeave={() => setHover(rating)}
                                >&#9733;</i> {/* Replace the star symbol with your sprites */}
                            </label>
                        );
                    })}
                </div>
            </Form.Group>
        </Form>
    );
}

export default RatingForm;
